// WindowsHelperDlg.cpp : implementation file
//

#include "stdafx.h"
#include "WindowsHelper.h"
#include "WindowsHelperDlg.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif

// CWindowsHelperDlg dialog

CWindowsHelperDlg::CWindowsHelperDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CWindowsHelperDlg::IDD, pParent)
    , m_Name(_T(""))
    , m_Value(_T(""))
	, m_bAutoRun(false)
{
	m_hIcon = AfxGetApp()->LoadIcon(IDR_MAINFRAME);
}

void CWindowsHelperDlg::DoDataExchange(CDataExchange* pDX)
{
    CDialog::DoDataExchange(pDX);
    DDX_Control(pDX, IDC_EDITNAME, m_edtName);
    DDX_Control(pDX, IDC_EDITVALUE, m_edtValue);
}

BEGIN_MESSAGE_MAP(CWindowsHelperDlg, CDialog)
	ON_WM_PAINT()
	ON_WM_QUERYDRAGICON()
	//}}AFX_MSG_MAP
    ON_BN_CLICKED(IDOK, &CWindowsHelperDlg::OnBnClickedOk)
    ON_BN_CLICKED(IDCANCEL, &CWindowsHelperDlg::OnBnClickedCancel)
	ON_MESSAGE (WM_AUTORUN, OnAutoRun)
END_MESSAGE_MAP()


// CWindowsHelperDlg message handlers

BOOL CWindowsHelperDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	// Set the icon for this dialog.  The framework does this automatically
	//  when the application's main window is not a dialog
	SetIcon(m_hIcon, TRUE);			// Set big icon
	SetIcon(m_hIcon, FALSE);		// Set small icon

	if (m_bAutoRun == false) {
		// no cmd line parameter - show the dialog
		ShowWindow(SW_NORMAL);
	} else {
		ShowWindow(SW_HIDE);
		PostMessage(WM_AUTORUN, 0, 0);
	}
	return TRUE;  // return TRUE  unless you set the focus to a control
}

// If you add a minimize button to your dialog, you will need the code below
//  to draw the icon.  For MFC applications using the document/view model,
//  this is automatically done for you by the framework.

void CWindowsHelperDlg::OnPaint()
{
	if (IsIconic())
	{
		CPaintDC dc(this); // device context for painting

		SendMessage(WM_ICONERASEBKGND, reinterpret_cast<WPARAM>(dc.GetSafeHdc()), 0);

		// Center icon in client rectangle
		int cxIcon = GetSystemMetrics(SM_CXICON);
		int cyIcon = GetSystemMetrics(SM_CYICON);
		CRect rect;
		GetClientRect(&rect);
		int x = (rect.Width() - cxIcon + 1) / 2;
		int y = (rect.Height() - cyIcon + 1) / 2;

		// Draw the icon
		dc.DrawIcon(x, y, m_hIcon);
	}
	else
	{
		CDialog::OnPaint();
	}
}

// The system calls this function to obtain the cursor to display while the user drags
//  the minimized window.
HCURSOR CWindowsHelperDlg::OnQueryDragIcon()
{
	return static_cast<HCURSOR>(m_hIcon);
}


void CWindowsHelperDlg::OnBnClickedOk()
{
    // call the method to set the environment
	if ((!m_Name.IsEmpty()) && (!m_Value.IsEmpty())) SetEnv();
    OnOK();
}

void CWindowsHelperDlg::OnBnClickedCancel()
{
    // skip setting the environment and exit
    OnCancel();
}

int CWindowsHelperDlg::ParseCmdLine(CString sCmdLine)
{
    CString sName = sCmdLine.Left(sCmdLine.Find(_T("=")));
    CString sValue = sCmdLine.Mid(sCmdLine.Find(_T("="))+1);
    sName = sName.Trim();
    sValue = sValue.Trim();

    m_Name = sName;
    m_Value = sValue;
    return 0;
}

int CWindowsHelperDlg::SetEnv(void)
{
    HKEY hKey;
	unsigned char tmp[4096];
	CString sTmp;
	DWORD cbData;
	// look for %variable% on the command line - do a value substitution if it is found
	if (this->m_Value.Find('%') >= 0) {
		// get value of path 
		if (RegOpenKeyEx(HKEY_LOCAL_MACHINE, _T("System\\CurrentControlSet\\Control\\Session Manager\\Environment"), 0, KEY_ALL_ACCESS, &hKey) == ERROR_SUCCESS) {
			//strcpy_s((char *) &tmp[0], 255, m_Value.GetBuffer());
			RegQueryValueEx(hKey, _T("PATH"), NULL, NULL, &tmp[0], &cbData);
		}
		// substitute it in to the value
		int nCount = m_Value.GetLength();
		nCount = nCount - m_Value.Find(';');
		sTmp = m_Value.Right(nCount);
		m_Value = tmp;
		m_Value.Append(sTmp);
	}
    // set local environment variable
    SetEnvironmentVariable(m_Name, m_Value);
    // set the registry key for the environment
    // HKEY_LOCAL_MACHINE\System\CurrentControlSet\Control\Session Manager\Environment 
    // Open registry key and save it to file
    if (RegOpenKeyEx(HKEY_LOCAL_MACHINE, _T("System\\CurrentControlSet\\Control\\Session Manager\\Environment"), 0, KEY_ALL_ACCESS, &hKey) == ERROR_SUCCESS) {
		strcpy_s((char *) &tmp[0], 4096, m_Value.GetBuffer());
        RegSetValueEx(hKey, m_Name, 0, REG_SZ, &tmp[0], 255);
    }
    return 0;
}

LRESULT CWindowsHelperDlg::OnAutoRun(WPARAM wParam, LPARAM lParam){
	unsigned char tmp[255];
 	
	ShowWindow(SW_HIDE);
	memset(tmp, 0, 255);
	strcpy_s((char *) &tmp[0], 255, _T("Environment"));
    //post message to all applications to read environment
	//PostMessage(WM_SETTINGCHANGE, 0, (LPARAM) &tmp[0]);
	// must post message to all top level windows
	PDWORD pResult;
	SendMessageTimeout(HWND_BROADCAST, WM_SETTINGCHANGE, 0, (LPARAM) &tmp[0], SMTO_NORMAL, 900000, (PDWORD_PTR)&pResult);
	OnOK();
	return 0;
}