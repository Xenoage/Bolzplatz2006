<?

    # ====================================================== #
    # BP2k6 - Homepage => News                               #
    # ------------------------------------------------------ #
    # (c)2006 maus / m.schmitz / maussites.de                #
    # LastChange: 02.04.2006                                 #
    # ====================================================== #


# status / todo:  o.k.
# ______________________________________________________________________________

require_once ("_config.inc.php");
# ______________________________________________________________________________

require_once ("../bpadmin/settings.inc.php");
#$db=mysql_connect($dbhost,$dbuser,$dbpass);                                     # open db
#mysql_select_db($dbname);

################################################################# Head ausgeben
require_once ('../global/_head.inc.php');

################################################################# CONTENT
echo '<span class="head"><B>Voluntary donations</B></span><P>';

?>
Slam Soccer 2006 is freeware, but we invested much time and effort in this project.<P>
If you want to support us, there are different opportunities:<BR>
You can buy the <a href="goldedition.php">Gold Edition</a> or <a href="http://bolzplatz2006.spreadshirt.de" TARGET="_blank">T-Shirts</a> or you can donate money.
<P><b>Thank you very much!</b>

<br><br><br>

PayPal:<br><br>

<form action="https://www.paypal.com/cgi-bin/webscr" method="post" target="_top">
<input type="hidden" name="cmd" value="_s-xclick">
<input type="hidden" name="hosted_button_id" value="GT297HN7TALW8">
<input type="image" src="https://www.paypalobjects.com/de_DE/DE/i/btn/btn_donateCC_LG.gif" border="0" name="submit" alt="Jetzt einfach, schnell und sicher online bezahlen – mit PayPal.">
<img alt="" border="0" src="https://www.paypalobjects.com/de_DE/i/scr/pixel.gif" width="1" height="1">
</form>


<?


################################################################# Footer ausgeben
require_once ('../global/_footer.inc.php');

#mysql_close($db);                                                               # close db
?>
