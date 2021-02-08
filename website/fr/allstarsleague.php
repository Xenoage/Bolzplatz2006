<?

    # ====================================================== #
    # BP2k6 - Homepage => HoF: AllstarsLeague                #
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
echo '<span class="head"><B>Hall of Fame / Allstars League</B></span><P>';

$htext01 = "Joueurs registr&eacute;s: ";
$htext02 = "Pays: ";
$htext03 = "Date: ";
$htext04 = "Nom: ";
$htext05 = "Dernière  inscription: ";
$htext06 = "Retourner";
$htext07 = "[Voir tous]";
$htext08 = "Il n'y a pas encore des enregistrements";
$htext09 = "Equipe cr&eacute;&eacute;es par les joueurs";
$htext10 = "Nom d'équipe: ";
$htext11 = "Diese Teamnamen wurden von Usern selbst erstellt. Die aufgeführten Teams stehen in keinem Zusammenhang mit möglicherweise realen Mannschaften!";
$htext12 = "Tous les champions de: ";
$htext20 = "Coupe du monde";
$htext21 = "Ligue Allstars";

################################################################################
$GetSql = mysql_query("SELECT AutomHoF,ShowOTeams FROM bpmain WHERE Art='settings'");
$XTemp = mysql_fetch_row($GetSql);
$AutomHoF = $XTemp[0];
$ShowOwnTeamName = $XTemp[1];
require_once ('../global/cms/hof-career.inc.php');
if ($ShowOwnTeamName == "1")
    {
    echo '<span class="alarm"></B><BR>'.$htext11.'</span>';
    }

################################################################# Footer ausgeben
require_once ('../global/_footer.inc.php');

mysql_close($db);                                                               # close db
?>
