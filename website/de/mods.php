<?

    # ====================================================== #
    # BP2k6 - Homepage => Mods                               #
    # ------------------------------------------------------ #
    # (c)2006 maus / m.schmitz / maussites.de                #
    # LastChange: 30.07.2006                                 #
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

$lang = "de";
$lang_author = "Autor";
$lang_text = "Sortiert nach";
$lang_team = "Team";
$lang_user = "Autor";
$lang_date = "Datum";
$lang_eintraege = "Einträge";

if (!$_GET["view"])                      #### Zeige Auswahl der Kategorien
    {
    ?><span class="head"><B>Mods</B></span> <P>
    <?

?><CENTER><TABLE BORDER="0" CELLSPACING="1" CELLPADDING="2" width="475"><?
###############################################################################################
                                                                                             ##

require ('../global/cms/mods-uebersicht.inc.php');   # Neue Kategorie? In dieser Datei eintragen !

                                                                                             ##
###############################################################################################
?><TR><TD COLSPAN="3"><HR SIZE="1"></TD></TR></TABLE></CENTER><?
    }
    else                                            #### Zeige gewaehlte Kategorie
        {
        $KatID = $_GET["view"];
        $Anzahl = mysql_num_rows(mysql_query("SELECT * FROM mods2_data WHERE type='$KatID'"));
        require ('../global/cms/modsview.inc.php');
        }

################################################################# Footer ausgeben
require_once ('../global/_footer.inc.php');

mysql_close($db);                                                               # close db

# ------------------------------------------------------------------------------    ausgabe uebersicht

function Ausgabe($KatID,$ThisLanguage,$lang_eintraege,$PrevPict) {
?><TR><TD COLSPAN="3"><HR SIZE="1"></TD></TR><TR><TD VALIGN="Middle"><a href="?view=<? echo $KatID; ?>"><img src="<? echo $PrevPict; ?>" border="0"></a></TD><?
$Anzahl = mysql_num_rows(mysql_query("SELECT * FROM mods2_data WHERE type='$KatID'"));
$XTemp = mysql_fetch_row(mysql_query("SELECT * FROM mods2_rubrik WHERE ID='$KatID'"));
$KatName = $XTemp[$ThisLanguage+1];
echo "<TD VALIGN=\"Middle\"><NOBR><span class=\"head\"><B>$KatName</B></span><BR>$Anzahl $lang_eintraege</TD>";
$XTemp = mysql_fetch_row(mysql_query("SELECT * FROM bpmain"));
$Button = $XTemp[$ThisLanguage+8];
echo "<TD VALIGN=\"Middle\"><a href=\"?view=$KatID\"><B>$Button &gt;&gt;</B></a></TD>";
echo "</TR>";
}

?>
