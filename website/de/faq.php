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
echo '<a name="pagetop"></a>';
echo '<span class="head"><B>Informationen / FAQ</B></span><P>';

$ThisLanguage --;
require_once ('../global/cms/faq.inc.php');


################################################################# Footer ausgeben
require_once ('../global/_footer.inc.php');

mysql_close($db);                                                               # close db
?>
