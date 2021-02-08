<?

    # ====================================================== #
    # BP2k6 - Admin -> Uebersicht                            #
    # ------------------------------------------------------ #
    # (c)2006 maus / m.schmitz / maussites.de                #
    # LastChange: 22.05.2006                                 #
    # ====================================================== #


# status / todo:  work...
# ______________________________________________________________________________


?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2//EN">
<HTML>
<HEAD>
<link rel=stylesheet type=text/css href=bpadmin.css>
</HEAD>
<BODY>

<CENTER><TABLE BORDER="0" CELLSPACING="1" CELLPADDING="7" class="outertablebg" WIDTH="730" HEIGHT="92%">
<TR><TD VALIGN="Top" HEIGHT="10">
<span class="head"><B>BP2k6</B> -&gt; Admin</B></span>
</TD></TR>
<TR><TD VALIGN="Top" HEIGHT="5" class="marktabletd">
<? require_once ('nav.inc.php'); ?>
</TD></TR>

<TR><TD class="tabletd" VALIGN="Top">

<!-- CONTENT START -------------------------------------------------------------###### -->

<?
require_once ("settings.inc.php");
$db=mysql_connect($dbhost,$dbuser,$dbpass);                                     # open db
mysql_select_db($dbname);
$Message = "hallo admin :)";

if ($_POST["do"] == "SaveNewsChars")
    {
    $ThisNew = $_POST["NewsChars"];
    if ($ThisNew > 0 && $ThisNew < 5) {$ThisNew = 5;}
    mysql_query("UPDATE bpmain SET NewsChars='$ThisNew' WHERE Art='settings' LIMIT 1");
    $Message = "News-Zeichen ge&auml;ndert -&gt; $ThisNew";
    }
if ($_POST["do"] == "SavePresseChars")
    {
    $ThisNew = $_POST["PresseChars"];
    if ($ThisNew > 0 && $ThisNew < 5) {$ThisNew = 5;}
    mysql_query("UPDATE bpmain SET PresseChars='$ThisNew' WHERE Art='settings' LIMIT 1");
    $Message = "Presse-Zeichen ge&auml;ndert -&gt; $ThisNew";
    }
if ($_POST["do"] == "SaveButtons")
    {
    $Btn_D = $_POST["Btn_D"];
    $Btn_E = $_POST["Btn_E"];
    $Btn_F = $_POST["Btn_F"];
    mysql_query("UPDATE bpmain SET DBtnBez='$Btn_D', EBtnBez='$Btn_E', FBtnBez='$Btn_F' WHERE Art='settings'");
    $Message = "Button-Bezeichnungen ge&auml;ndert";
    }
if ($_POST["do"] == "SaveHoF")
    {
    $viewown = $_POST["viewown"];
    $autoown = $_POST["autoown"];
    mysql_query("UPDATE bpmain SET AutomHoF='$autoown', ShowOTeams='$viewown' WHERE Art='settings'");
    $Message = "Hall of Fame Einstellungen ge&auml;ndert";
    }

echo "<CENTER><B>Nachricht: $Message</B></Center><HR SIZE='1'> <BR> ";
echo '<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="8" WIDTH="100%" class="outertablebg" WIDTH="100%" HEIGHT="100%">';
echo '<TR><TD VALIGN="Top" class="tabletd" WIDTH="50%">';

########################################## LINKS ###############################

## Ausgabe Downloads Homepage---------------------------------------------------
echo '<span class="phatt"><LI><B>Info: Downloads Homepage</B></span><P>';
$GetSql = mysql_query("SELECT * FROM download ORDER BY Prg");
echo '<TABLE BORDER="0" CELLSPACING="1" CELLPADDING="3" class="outertablebg" width="100%">';
while ($datensatz = mysql_fetch_row($GetSql))
       {
       $FName = explode(".", $datensatz[0]);
       $TStamp = explode(" ", $datensatz[2]);
       $ODate = explode("-", $TStamp[0]);
       $MyDate =  $ODate[2].".".$ODate[1].".".$ODate[0].",";
       echo "<TR><TD VALIGN='Top' class='tabletd'><B>".$FName[0]."</B>.".$FName[1]."</TD>";
       echo "<TD VALIGN='Top' align='Right' class='tabletd'><B>".$datensatz[1]."</B></TD>";
       echo "<TD VALIGN='Top' align='Right' class='tabletd'>last: ".$MyDate.$TStamp[1]."</TD></TR>";
       }
echo '</TABLE>';
echo '<BR><HR SIZE="1">';
## -----------------------------------------------------------------------------

## Ausgabe Downloads Extern-----------------------------------------------------
/*
echo '<span class="phatt"><LI><B>Info: Downloads von extern</B></span><P>';
$GetSql = mysql_query("SELECT * FROM download_ext ORDER BY Prg");
echo '<TABLE BORDER="0" CELLSPACING="1" CELLPADDING="3" class="outertablebg" width="100%">';
while ($datensatz = mysql_fetch_row($GetSql))
       {
       $FName = explode(".", $datensatz[0]);
       $TStamp = explode(" ", $datensatz[2]);
       $ODate = explode("-", $TStamp[0]);
       $MyDate =  $ODate[2].".".$ODate[1].".".$ODate[0].",";
       echo "<TR><TD VALIGN='Top' class='tabletd'><B>".$FName[0]."</B>.".$FName[1]."</TD>";
       echo "<TD VALIGN='Top' align='Right' class='tabletd'><B>".$datensatz[1]."</B></TD>";
       echo "<TD VALIGN='Top' align='Right' class='tabletd'>last: ".$MyDate.$TStamp[1]."</TD></TR>";
       }
echo '</TABLE>';
echo '<BR><HR SIZE="1">';
## -----------------------------------------------------------------------------
*/

## Ausgabe HoF Info-------------------------------------------------------------
echo '<span class="phatt"><LI><B>Info: Hall of Fame</B></span><P>';
$GetSql = mysql_query("SELECT * FROM hof_user");
$Anzahl = mysql_num_rows($GetSql);
echo "<TT><B>Allstars League: </TT>".$Anzahl." </B>Einträge ".'&nbsp;&nbsp;<a href="../de/allstarsleague.php" TARGET="_blank">[view]</a><BR>';
$GetSql = mysql_query("SELECT * FROM hof_user_wc");
$Anzahl = mysql_num_rows($GetSql);
echo "<TT><B>WM&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;: </TT>".$Anzahl." </B>Einträge ".'&nbsp;&nbsp;<a href="../de/wm.php" TARGET="_blank">[view]</a>';
echo '<BR><BR><HR SIZE="1">';
## -----------------------------------------------------------------------------

## HoF Settings ----------------------------------------------------------------
echo '<span class="phatt"><LI><B>Hall of Fame Settings</B></span><P>';
$GetSql = mysql_query("SELECT * FROM bpmain WHERE Art='settings'");
$XTemp = mysql_fetch_row($GetSql);
echo '<FORM METHOD="Post" ACTION="index.php">';
echo '<input type="hidden" name="do" value="SaveHoF">';
echo 'Sollen selbsterstellte Teamnamen angezeigt werden?<BR>';
$markme = ""; if ($XTemp[13] == "1") {$markme = "checked";}
echo '<INPUT TYPE="radio" NAME="viewown" VALUE="1" '.$markme.'> - ja &nbsp;&nbsp;&nbsp;';
$markme = ""; if ($XTemp[13] == "0") {$markme = "checked";}
echo '<INPUT TYPE="radio" NAME="viewown" VALUE="0" '.$markme.'> - nein<P>';
echo 'Selbsterstellte Teams automatisch freigeschalten?<BR> ';
$markme = ""; if ($XTemp[12] == "0") {$markme = "checked";}
echo '<INPUT TYPE="radio" NAME="autoown" VALUE="0" '.$markme.'> - ja &nbsp;&nbsp;&nbsp;';
$markme = ""; if ($XTemp[12] == "1") {$markme = "checked";}
echo '<INPUT TYPE="radio" NAME="autoown" VALUE="1" '.$markme.'> - nein, erst durch Admin<br>';
echo '<BR><input type="submit" value="speichern"></FORM><HR SIZE="1">';
echo '</form>';

#echo '<TT><B>WM:</B></TT> 0 Mannschaften / 0 Eintr&auml;ge<BR>';
#echo '<TT><B>AL:</B></TT> 0 Mannschaften / 0 Eintr&auml;ge<BR>';

## -----------------------------------------------------------------------------



########################################## MITTE / TRENNEN #####################

echo '</TD>';
echo '<TD class="tabletd" background="grfx/trenner.gif">&nbsp;&nbsp;</TD>';
echo '<TD VALIGN="Top" class="tabletd" WIDTH="50%">';



########################################## RECHTS ##############################

## NEWS / Max. Zeichen ---------------------------------------------------------
echo '<span class="phatt"><LI><B>News:</B></span>';
$GetSql = mysql_query("SELECT NewsChars FROM bpmain WHERE Art='settings'");
$XTemp = mysql_fetch_row($GetSql);
echo '<FORM METHOD="Post" ACTION="index.php">';
echo '<input type="hidden" name="do" value="SaveNewsChars">';
echo 'News nach ';
echo '<input type="text" name="NewsChars" size="2" maxlength="5" VALUE="'.$XTemp[0].'"> Wörtern abschneiden -&gt; Folgeseite.';
echo '<BR>( 0 = nie abschneiden )<p>';
echo '<input type="submit" value="speichern"></FORM><HR SIZE="1">';
## -----------------------------------------------------------------------------

## PRESSE / Max. Zeichen -------------------------------------------------------
echo '<span class="phatt"><LI><B>Presse:</B></span>';
$GetSql = mysql_query("SELECT PresseChars FROM bpmain WHERE Art='settings'");
$XTemp = mysql_fetch_row($GetSql);
echo '<FORM METHOD="Post" ACTION="index.php">';
echo '<input type="hidden" name="do" value="SavePresseChars">';
echo 'Presse nach ';
echo '<input type="text" name="PresseChars" size="2" maxlength="5" VALUE="'.$XTemp[0].'"> Wörtern abschneiden -&gt; Folgeseite.';
echo '<BR>( 0 = nie abschneiden )<p>';
echo '<input type="submit" value="speichern"></FORM><HR SIZE="1">';
## -----------------------------------------------------------------------------

## CMS Buttons -----------------------------------------------------------------
echo '<span class="phatt"><LI><B>CMS-Buttons:</B></span>';
$GetSql = mysql_query("SELECT * FROM bpmain WHERE Art='settings'");
$XTemp = mysql_fetch_row($GetSql);
echo '<FORM METHOD="Post" ACTION="index.php">';
echo '<input type="hidden" name="do" value="SaveButtons">';
echo '"weiter"-Button Bezeichnungen f&uuml;r: <P>';
echo '<B><TT>Deutsch:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TT></B> <input type="text" name="Btn_D" size="20" maxlength="25" VALUE="'.$XTemp[9].'"><BR>';
echo '<B><TT>Englisch:&nbsp;&nbsp;&nbsp;&nbsp;</TT></B> <input type="text" name="Btn_E" size="20" maxlength="25" VALUE="'.$XTemp[10].'"><BR>';
echo '<B><TT>Französisch:&nbsp;</TT></B> <input type="text" name="Btn_F" size="20" maxlength="25" VALUE="'.$XTemp[11].'"><BR>';
echo '<BR><input type="submit" value="speichern"></FORM><HR SIZE="1">';
## -----------------------------------------------------------------------------


echo '</TD></TR>';
echo '</TABLE>';

mysql_close($db);
?>

<!-- CONTENT END ---------------------------------------------------------------###### -->

</TD></TR>
</TABLE></CENTER>

</BODY>
</HTML>


<?
# -----  Funktionen   ##########################################################


?>