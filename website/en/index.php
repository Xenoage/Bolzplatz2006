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
mysql_set_charset('latin1',$db); //latin1 = windows-1252
mysql_select_db($dbname);

################################################################# Head ausgeben
require_once ('../global/_head.inc.php');

################################################################# CONTENT
echo '<span class="head"><B>Home / News</B></span><br>';
//echo '<HR SIZE="1"><b>Hint:</b> If you subscribe to our <a href="../en-newsletter/newsletter.php"><b>newsletter</b></a>, you\'ll stay informed about all news and updates.<HR SIZE="1"><P>';
require_once ('../global/cms/news.inc.php');



################################################################# Footer ausgeben
require_once ('../global/_footer.inc.php');

mysql_close($db);                                                               # close db
?>
