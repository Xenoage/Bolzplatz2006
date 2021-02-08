<?

    # ====================================================== #
    # BP2k6 - Homepage => Mods                               #
    # ------------------------------------------------------ #
    # (c)2006 maus / m.schmitz / maussites.de                #
    # LastChange: 19.06.2006 (Andi)                          #
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
#echo '<span class="head"><B>Teams</B></span><P>';

$lang = "fr";
$lang_author = "Author";
$lang_text = "Sort by";
$lang_team = "Team";
$lang_user = "Author";
$lang_date = "Date";

$lang_headline = "Teams";    # Ueberschrift der Tabelle
$ViewMode = "team";          # MySQL Feldname fuer Auswahl
$files_dirname = "teams";    # Ordnername für Download- bzw. Grafikverzeichnis
require ('../global/cms/mods-teams.inc.php');

echo "<BR><BR><BR>";

$lang_headline = "Stadiums"; # Ueberschrift der Tabelle
$ViewMode = "stadium";       # MySQL Feldname fuer Auswahl
$files_dirname = "stadiums"; # Ordnername für Download- bzw. Grafikverzeichnis
require ('../global/cms/mods-teams.inc.php');


################################################################# Footer ausgeben
require_once ('../global/_footer.inc.php');

mysql_close($db);                                                               # close db
?>