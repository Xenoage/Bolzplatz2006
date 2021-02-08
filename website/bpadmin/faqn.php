<?

    # ====================================================== #
    # BP2k6 - Admin -> FAQSystem Fragen/Antworten anlegen    #
    # ------------------------------------------------------ #
    # (c)2006 maus / m.schmitz / maussites.de                #
    # LastChange: 20.04.2006                                 #
    # ====================================================== #


# status / todo:  o.k.
# ______________________________________________________________________________

?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2//EN">
<HTML>
<HEAD>
<link rel=stylesheet type=text/css href=bpadmin.css>
</HEAD>
<BODY>

<CENTER><TABLE BORDER="0" CELLSPACING="1" CELLPADDING="7" class="outertablebg" WIDTH="650" HEIGHT="92%">
<TR><TD VALIGN="Top" HEIGHT="10">
<span class="head"><B>BP2k6</B> -&gt; Admin -&gt; FAQ</B></span>
</TD></TR>
<TR><TD VALIGN="Top" HEIGHT="5" class="marktabletd">
<? require_once ('nav.inc.php'); ?>
</TD></TR>

<TR><TD class="tabletd" VALIGN="Top" HEIGHT="99%">

<?
require_once ("settings.inc.php");
$db=mysql_connect($dbhost,$dbuser,$dbpass);                                     # open db
mysql_select_db($dbname);

if ($DebugMode == "1") {echo ">>> get:".$_GET["do"]." >> post:".$_POST["do"]."<P>";}

$donow = $_GET["do"];




if ($_GET["do"] == "NeueAntwort")
   {
   #NeueAntwort ();               # Anzeigen aller Themen
   ?>
   <FORM METHOD="Post" ACTION="?do=SaveNeueAntwort">
   <?
   $ThisID = $_GET["id"];
   echo '<input type="hidden" name="id" value="'.$ThisID.'">';
   ?>
   <B>Neue Antwort anlegen:</B><P>
   <?
   $GetSql = mysql_query("SELECT * FROM faq WHERE FaqID='$ThisID'");
   $XOrg = mysql_fetch_row($GetSql);
   NeueAntwort ($XOrg,$EditID);                   # inputfeld fuer neues thema anzeigen
   echo '<input type="submit" value="Antwort(en) speichern">';
   echo '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="faq.php?do=0"><B>&gt;&gt; [Abbrechen] &lt;&lt;</B></a>';
   echo '</FORM>';
   }

if ($_GET["do"] == "SaveNeueAntwort")
    {
    $ThemaID = $_POST["id"];
    AntwortSpeichern ($ThemaID);
    }

if ($_GET["do"] == "NeuesThema") {
   ?>
   <FORM METHOD="Post" ACTION="?do=SaveNeuesThema">
   <B>Neues FAQ-Thema anlegen:</B><P>
   <?
   NeuesThema ($XOrg,$EditID);                   # inputfeld fuer neues thema anzeigen
   echo '<input type="submit" value="Neues Thema anlegen">';
   echo '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="faq.php?do=0"><B>&gt;&gt; [Abbrechen] &lt;&lt;</B></a>';
   echo '</FORM>';
   }

if ($donow == "SaveNeuesThema")
   {
   NeuesThemaSave ();            # neues thema speichern
   ReRun ($DebugMode,$Message='Thema erstellt!');
   }

if ($DebugMode == "1") {echo "<P><B>>> db closed.</B><P>";}
mysql_close($db);                                                               # close DB
?>

</TD></TR>
</TABLE></CENTER>
</BODY>
</HTML>



<?

## ----------------------------------------------------------------------------- Funktionen

#______________________________________________________________________  ####### Antwort speichern

function AntwortSpeichern ($ThemaID) {
        $GetSql = mysql_query("SELECT * FROM faq WHERE Art='antwort' ORDER BY Antw_Sort desc");
        $XTemp = mysql_fetch_row($GetSql);
        #echo "<P>".$XTemp[1];
        $NewId = $XTemp[6] +1;
        $RName_d = $_POST["RName_d"];
        $RName_e = $_POST["RName_e"];
        $RName_f = $_POST["RName_f"];
        $FName_d = $_POST["FName_d"];
        $FName_e = $_POST["FName_e"];
        $FName_f = $_POST["FName_f"];
        $timenow = date("d.m.Y",time());
        mysql_query("INSERT INTO faq (Sort, Text_d, Text_e, Text_f, Art, Antw_Sort, TFaqID, Date, SubHead_d, SubHead_e, SubHead_f )
                     VALUES ('0', '$RName_d', '$RName_e', '$RName_f', 'antwort', '$NewId', '$ThemaID', '$timenow', '$FName_d', '$FName_e', '$FName_f' )"
                     );
        ReRun ($DebugMode,$Message='Antwort gespeichert!');
}


#______________________________________________________________________  ####### Input neue Antwort

function NeueAntwort ($XOrg,$EditID) {
    ?>
    <TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" WIDTH="100%" class="outertablebg">
    <TR>
    <TD><IMG SRC="grfx/flag_d.gif">&nbsp;&nbsp;&nbsp;<span class="head"><B>Deutsch</B></span></TD></TR>
    <TR>
    </TABLE>
    <? if ($XOrg[2] != "") {
    echo "<P><LI><B>".$XOrg[2]."</B>"; ?>
    <TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" class="outertablebg">
    <TR>
    <TD VALIGN="Top" class="tabletd">&nbsp; <BR><B>Frage:</B></TD>
    <TD VALIGN="Top" class="tabletd">&nbsp;&nbsp;&nbsp;</TD>
    <TD VALIGN="Top" class="tabletd">&nbsp; <BR><textarea name="FName_d" rows="4" cols="80"><? echo $MyOrg[2]; ?></textarea></TD>
    </TR><TR>
    <TD VALIGN="Top" class="tabletd"><B>Antwort:</B></TD>
    <TD VALIGN="Top" class="tabletd">&nbsp;&nbsp;&nbsp;</TD>
    <TD VALIGN="Top" class="tabletd"><textarea name="RName_d" rows="4" cols="80"><? echo $MyOrg[2]; ?></textarea>&nbsp; <BR> </TD>
    </TR>
    </TABLE>
    <? } else {echo "</B><P><I>Thema ist hier nicht vorhanden !</I><P>";} ?>

    <TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" WIDTH="100%" class="outertablebg">
    <TR>
    <TD><IMG SRC="grfx/flag_e.gif">&nbsp;&nbsp;&nbsp;<span class="head"><B>English</B></span></TD></TR>
    <TR>
    </TABLE>
    <? if ($XOrg[3] != "") {
    echo "<P><LI><B>".$XOrg[3]."</B>"; ?>
    <TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" class="outertablebg">
    <TR>
    <TD VALIGN="Top" class="tabletd">&nbsp; <BR><B>Frage:</B></TD>
    <TD VALIGN="Top" class="tabletd">&nbsp;&nbsp;&nbsp;</TD>
    <TD VALIGN="Top" class="tabletd">&nbsp; <BR><textarea name="FName_e" rows="4" cols="80"><? echo $MyOrg[3]; ?></textarea></TD>
    </TR><TR>
    <TD VALIGN="Top" class="tabletd"><B>Antwort:</B></TD>
    <TD VALIGN="Top" class="tabletd">&nbsp;&nbsp;&nbsp;</TD>
    <TD VALIGN="Top" class="tabletd"><textarea name="RName_e" rows="4" cols="80"><? echo $MyOrg[3]; ?></textarea>&nbsp; <BR> </TD>
    </TR>
    </TABLE>
    <? } else {echo "</B><P><I>Thema ist hier nicht vorhanden !</I><P>";} ?>

    <TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" WIDTH="100%" class="outertablebg">
    <TR>
    <TD><IMG SRC="grfx/flag_f.gif">&nbsp;&nbsp;&nbsp;<span class="head"><B>Français</B></span></TD></TR>
    <TR>
    </TABLE>
    <? if ($XOrg[4] != "") {
    echo "<P><LI><B>".$XOrg[4]."</B>"; ?>
    <TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" class="outertablebg">
    <TR>
    <TD VALIGN="Top" class="tabletd">&nbsp; <BR><B>Frage:</B></TD>
    <TD VALIGN="Top" class="tabletd">&nbsp;&nbsp;&nbsp;</TD>
    <TD VALIGN="Top" class="tabletd">&nbsp; <BR><textarea name="FName_f" rows="4" cols="80"><? echo $MyOrg[4]; ?></textarea></TD>
    </TR><TR>
    <TD VALIGN="Top" class="tabletd"><B>Antwort:</B></TD>
    <TD VALIGN="Top" class="tabletd">&nbsp;&nbsp;&nbsp;</TD>
    <TD VALIGN="Top" class="tabletd"><textarea name="RName_f" rows="4" cols="80"><? echo $MyOrg[4]; ?></textarea>&nbsp; <BR> </TD>
    </TR>
    </TABLE>
    <? } else {echo "</B><P><I>Thema ist hier nicht vorhanden !</I><P>";} ?>

    <HR SIZE="1">
    <?
}


#______________________________________________________________________  ####### Input neues Thema

function NeuesThema ($XOrg,$EditID) {
    ?>
    <TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" WIDTH="100%" class="outertablebg">
    <TR>
    <TD><IMG SRC="grfx/flag_d.gif">&nbsp;&nbsp;&nbsp;<span class="head"><B>Deutsch</B></span></TD></TR>
    <TR>
    </TABLE>
    <TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" class="outertablebg">
    <TD VALIGN="Top" class="tabletd">&nbsp; <BR><B>Thema:</B></TD>
    <TD VALIGN="Top" class="tabletd">&nbsp;&nbsp;&nbsp;</TD>
    <TD VALIGN="Top" class="tabletd">&nbsp; <BR><input type="text" name="RName_d" size="80" maxlength="500" VALUE="<? echo $XOrg[2]; ?>">&nbsp; <BR> </TD>
    </TR></TABLE>
    <TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" WIDTH="100%" class="outertablebg">
    <TR>
    <TD><IMG SRC="grfx/flag_e.gif">&nbsp;&nbsp;&nbsp;<span class="head"><B>English</B></span></TD></TR>
    <TR>
    </TABLE>
    <TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" class="outertablebg">
    <TD VALIGN="Top" class="tabletd">&nbsp; <BR><B>Thema:</B></TD>
    <TD VALIGN="Top" class="tabletd">&nbsp;&nbsp;&nbsp;</TD>
    <TD VALIGN="Top" class="tabletd">&nbsp; <BR><input type="text" name="RName_e" size="80" maxlength="500" VALUE="<? echo $XOrg[3]; ?>">&nbsp; <BR> </TD>
    </TR></TABLE>
    <TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" WIDTH="100%" class="outertablebg">
    <TR>
    <TD><IMG SRC="grfx/flag_f.gif">&nbsp;&nbsp;&nbsp;<span class="head"><B>Français</B></span></TD></TR>
    <TR>
    </TABLE>
    <TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" class="outertablebg">
    <TD VALIGN="Top" class="tabletd">&nbsp; <BR><B>Thema:</B></TD>
    <TD VALIGN="Top" class="tabletd">&nbsp;&nbsp;&nbsp;</TD>
    <TD VALIGN="Top" class="tabletd">&nbsp; <BR><input type="text" name="RName_f" size="80" maxlength="500" VALUE="<? echo $XOrg[4]; ?>">&nbsp; <BR> </TD>
    </TR></TABLE>
    <HR SIZE="1">
    <?
}

#______________________________________________________________________  ####### Save neues Thema

function NeuesThemaSave () {
        $GetSql = mysql_query("SELECT * FROM faq WHERE Art='thema' ORDER BY Sort desc");
        $XTemp = mysql_fetch_row($GetSql);
        $NewId = $XTemp[1] +1;
        $RName_d = $_POST["RName_d"];
        $RName_e = $_POST["RName_e"];
        $RName_f = $_POST["RName_f"];
        $timenow = date("d.m.Y",time());
        mysql_query("INSERT INTO faq (Sort, Text_d, Text_e, Text_f, Art, Date )
                     VALUES ('$NewId', '$RName_d', '$RName_e', '$RName_f', 'thema', '$timenow' )"
                     );
}




#______________________________________________________________________  ####### ReLoad Browser / um moegliches cache-problem zu umgehen..

function ReRun ($DebugMode,$Message) {
   $ProID  = md5 (uniqid (rand()));
   $ReTime = 0;
   if ($DebugMode == "1") {echo '<B>'.$Message.'</B>&nbsp;&nbsp;&nbsp;&nbsp;<a href="faq.php?do=x&'.$ProID.'">(weiter)</a><HR SIZE="1"><P>'; $ReTime = 2; }
   echo '<META HTTP-EQUIV="Refresh" CONTENT="'.$ReTime.'; URL=faq.php?do=x&'.$ProID.'">';
}