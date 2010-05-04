// WindowsHelperDlg.h : header file
//

#pragma once
#include "afxwin.h"

#define WM_AUTORUN WM_APP + 0x100

// CWindowsHelperDlg dialog
class CWindowsHelperDlg : public CDialog
{
// Construction
public:
	CWindowsHelperDlg(CWnd* pParent = NULL);	// standard constructor

// Dialog Data
	enum { IDD = IDD_WINDOWSHELPER_DIALOG };

	protected:
	virtual void DoDataExchange(CDataExchange* pDX);	// DDX/DDV support


// Implementation
protected:
	HICON m_hIcon;

	// Generated message map functions
	virtual BOOL OnInitDialog();
	afx_msg void OnPaint();
	afx_msg HCURSOR OnQueryDragIcon();
	afx_msg LRESULT OnAutoRun(WPARAM wParam, LPARAM lParam);
	DECLARE_MESSAGE_MAP()
public:
    afx_msg void OnEnChangeEdit1();
    afx_msg void OnBnClickedOk();
    afx_msg void OnBnClickedCancel();
    CEdit m_edtName;
    CEdit m_edtValue;
    int ParseCmdLine(CString sCmdLine);
    int SetEnv(void);
    CString m_Name;
    CString m_Value;
	bool m_bAutoRun;
};

