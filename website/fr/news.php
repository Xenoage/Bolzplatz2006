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
$db=mysql_connect($dbhost,$dbuser,$dbpass);                                     # open db
mysql_select_db($dbname);

################################################################# Head ausgeben
require_once ('../global/_head.inc.php');

################################################################# CONTENT
echo '<span class="head"><B>Home / News</B></span><p>';
#echo '<HR SIZE="1"><b>Hinweis:</b> Trage Dich jetzt in den <a href="../de-newsletter/newsletter.php"><b>Bolzplatz 2006 Newsletter</b></a> ein, und wir informieren Dich über alle Updates!<HR SIZE="1"><P>';
require_once ('../global/cms/news.inc.php');

## ACHTUNG: Diese gleiche Datei ist noch mal als index.php in diesem Ordner vorhanden!

################################################################# Footer ausgeben
require_once ('../global/_footer.inc.php');

mysql_close($db);                                                               # close db
?>
