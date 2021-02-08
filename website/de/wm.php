<?

    # ====================================================== #
    # BP2k6 - Homepage => HoF: WM                            #
    # ------------------------------------------------------ #
    # (c)2006 maus / m.schmitz / maussites.de                #
    # LastChange: 07.05.2006                                 #
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
$ThisFile = basename($_SERVER["PHP_SELF"]);

################################################################# WM ausgeben
echo '<span class="head"><B>Hall of Fame / WM</B></span><P>';

$htext01 = "Registrierte Gewinner: ";
$htext02 = "aus ";
$htext03 = "am ";
$htext04 = "von ";
$htext05 = "letzter Eintrag: ";
$htext06 = "Zurück";
$htext07 = "[Alle ansehen]";
$htext08 = "Es sind noch keine Einträge vorhanden!";
$htext12 = "Alle Gewinner aus: ";
$htext20 = "Weltmeisterschaft";
$htext21 = "Allstars League";

################################################################################
$GetSql = mysql_query("SELECT AutomHoF,ShowOTeams FROM bpmain WHERE Art='settings'");
$XTemp = mysql_fetch_row($GetSql);
$AutomHoF = $XTemp[0];
$ShowOwnTeamName = $XTemp[1];
require_once ('../global/cms/hof-worldcup.inc.php');

################################################################# Footer ausgeben
require_once ('../global/_footer.inc.php');

mysql_close($db);                                                               # close db
?>
