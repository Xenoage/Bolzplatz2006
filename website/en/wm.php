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

$htext01 = "Registered winners: ";
$htext02 = "Location: ";
$htext03 = "Date: ";
$htext04 = "Name: ";
$htext05 = "Last entry: ";
$htext06 = "back";
$htext07 = "[view all]";
$htext08 = "Sorry - no entries!";
$htext12 = "All winners from: ";
$htext20 = "World Cup";
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
