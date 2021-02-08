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
echo '<span class="head"><B>Freiwillige Spenden</B></span><P>';

?>
Bolzplatz 2006 wird als Freeware ver&ouml;ffentlicht. Die Arbeit daran kostet uns aber viel Zeit und M&uuml;he.<BR>
Deshalb w&uuml;rden wir uns &uuml;ber Deine Unterst&uuml;tzung sehr freuen.<P>
Das kann der Kauf der <a href="goldedition.php">Gold-Edition </a> sein, Du kannst aber gerne auch Geld spenden.
<b><p>Herzlichen Dank im Vorraus!</b>
<P>
Unsere Bankverbindung geben Dir wir gerne per <a href="contact.php">E-Mail</a> (aus Sicherheitsgr&uuml;nden).<br><br>

<br><br>

Noch bequemer gehts &uuml;ber PayPal:<br><br>

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
