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
echo '<span class="head"><B>Vos dons</B></span><P>';

?>
Coup de Foot 2006 est (/) freeware.<BR>
Nous y avons mis beaucoup de travail et de temps.
<P>
Alors nous serons tr&egrave;s heureux de ton soutien. Tu peux nous soutenir en achetant <a href="goldedition.php">Gold Edition</a><BR>
ou un <a href="http://bolzplatz2006.spreadshirt.de" TARGET="_blank">T-shirt</a> de Coup de Foot 2006 ou simplement par un don financier.
<P>
<b>Merci beaucoup d'avance!  </b>

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
