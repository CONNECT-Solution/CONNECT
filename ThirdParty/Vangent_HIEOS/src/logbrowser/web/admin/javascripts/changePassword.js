function check()
{
    var pw = $('oldPassword').value ;
    if ( $('newPassword').value == $('reTypePassword').value )
    {
        chgPassword ( sha256_digest( pw ) , sha256_digest( $('newPassword').value ) ) ;
    }
    else
    {
        $('errorMsg').innerHTML = "<p style='color:red;'>The two passwords are differents</p>" ;
    }
   
}