<?

    # ====================================================== #
    # BP2k6 - Admin -> FAQSystem (Themen)                    #
    # ------------------------------------------------------ #
    # (c)2006 maus / m.schmitz / maussites.de                #
    # LastChange: 17.04.2006                                 #
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

if ($donow == "up")
    {
    ThemaUp ();                  # gewaehltes thema 1pos nach oben
    }
if ($donow == "upA")
    {
    AnwortUp ();                  # gewaehlte antwort 1pos nach oben
    }

if ($_POST["do"] == "delete")    # thema loeschen
   {
   $EditID = $_POST["VarID"];
   mysql_query("DELETE FROM faq WHERE FaqID='$EditID' LIMIT 1");
   mysql_query("DELETE FROM faq WHERE TFaqID='$EditID'");
   ReRun ($DebugMode,$Message='Thema geloescht!');
   }

if ($_POST["do"] == "edit")      # thema editieren
   {
   $EditID = $_POST["VarID"];
   echo '<FORM METHOD="Post" ACTION="?do=editsave">';
   echo '<input type="hidden" name="VarID" value="'.$EditID.'">';
   echo '<B>FAQ-Thema bearbeiten:</B><P>';
   $GetSql = mysql_query("SELECT * FROM faq WHERE FaqID='$EditID'");
   $XOrg = mysql_fetch_row($GetSql);
   EditThema ($XOrg,$EditID);
   echo '<input type="submit" value="&Auml;nderungen speichern">';
   echo '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="?do=0"><B>&gt;&gt; [Abbrechen] &lt;&lt;</B></a>';
   echo '<HR SIZE="1"></FORM>';
   }

if ($donow == "editsave")        # thema editieren speichern
   {
   $EditID = $_POST["VarID"];
   $RName_d = $donow = $_POST["RName_d"];
   $RName_e = $donow = $_POST["RName_e"];
   $RName_f = $donow = $_POST["RName_f"];
   mysql_query("UPDATE faq SET Text_d='$RName_d', Text_e='$RName_e', Text_f='$RName_f' WHERE FaqID='$EditID'");
   ReRun ($DebugMode,$Message='Thema geaendert!');
   }

if ($_POST["do"] == "Aedit")      # antwort editieren
   {
   $EditID = $_POST["VarID"];
   echo '<FORM METHOD="Post" ACTION="?do=Aeditsave">';
   echo '<input type="hidden" name="VarID" value="'.$EditID.'">';
   echo '<B>Antwort bearbeiten:</B><P>';
   $GetSql = mysql_query("SELECT * FROM faq WHERE FaqID='$EditID'");
   $XOrg = mysql_fetch_row($GetSql);
   EditAntwort ($XOrg,$EditID);
   echo '<input type="submit" value="&Auml;nderungen speichern">';
   echo '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="?do=0"><B>&gt;&gt; [Abbrechen] &lt;&lt;</B></a>';
   echo '<HR SIZE="1"></FORM>';
   }

if ($donow == "Aeditsave")        # antwort editieren speichern
   {
   $EditID = $_POST["VarID"];
   $RName_d = $donow = $_POST["RName_d"];
   $RName_e = $donow = $_POST["RName_e"];
   $RName_f = $donow = $_POST["RName_f"];
   $FName_d = $_POST["FName_d"];
   $FName_e = $_POST["FName_e"];
   $FName_f = $_POST["FName_f"];
   mysql_query("UPDATE faq SET Text_d='$RName_d', Text_e='$RName_e', Text_f='$RName_f', SubHead_d='$FName_d', SubHead_e='$FName_e', SubHead_f='$FName_f' WHERE FaqID='$EditID'");
   ReRun ($DebugMode,$Message='fAQ geaendert!');
   }

if (($_POST["do"] != "edit")and($_POST["do"] != "Aedit"))
   {
   ThemenAnzeigen ();               # Anzeigen aller Themen/Antworten
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


#______________________________________________________________________  ####### Thema 1pos nach oben

function ThemaUp () {
    $UpID = $_GET["id"];
    $PrevID = $_GET["previd"];
    if ($DebugMode == "1") {echo "Change: $UpID -> $PrevID";}
    mysql_query("UPDATE faq SET Sort='999' WHERE Sort='$UpID' LIMIT 1");
    mysql_query("UPDATE faq SET Sort='$UpID' WHERE Sort='$PrevID' LIMIT 1");
    mysql_query("UPDATE faq SET Sort='$PrevID' WHERE Sort='999' LIMIT 1");
}

#______________________________________________________________________  ####### Antwort 1pos nach oben

function AnwortUp () {
    $UpID = $_GET["id"];
    $PrevID = $_GET["previd"];
    if ($DebugMode == "1") {echo "Change: $UpID -> $PrevID";}
    mysql_query("UPDATE faq SET Antw_Sort='999' WHERE Antw_Sort='$UpID' LIMIT 1");
    mysql_query("UPDATE faq SET Antw_Sort='$UpID' WHERE Antw_Sort='$PrevID' LIMIT 1");
    mysql_query("UPDATE faq SET Antw_Sort='$PrevID' WHERE Antw_Sort='999' LIMIT 1");
}

#______________________________________________________________________  ####### Themen Anzeigen

function ThemenAnzeigen () {
?>
    <B>FAQ / vorhandene Themen:</B><P>
    <TABLE BORDER="0" CELLSPACING="1" CELLPADDING="2" class="outertablebg" width="100%">
    <?
    ### Fragen:
    $count = 1;
    $GetSql = mysql_query("SELECT * FROM faq WHERE Art='thema' ORDER BY Sort");
    $Anzahl = mysql_num_rows($GetSql);
    while ($datensatz = mysql_fetch_row($GetSql))
           {
           $MyUp = "";
           $ThisTID = $datensatz[0];
           if ($count != 1)                         # pos-verschieben Button..?
               {
               $ProID  = md5 (uniqid (rand()));
               $MyUp = '<a href="faq.php?do=up&id='.$datensatz[1].'&previd='.$PrevID.'&mo='.$ProID.'"><IMG SRC="grfx/p_up.gif" border="0"></a>';
               }
           echo '<TD VALIGN="Middle" class="marktabletd" align="center">'.$MyUp.'</TD>';

           if ($datensatz[2] != "")                 # Eine der DREI Headlines wird doch wohl ausgefuellt sein..? :)
               {
               $ThemaText = $datensatz[2];
               }
               elseif ($datensatz[3] != "")
                       {
                       $ThemaText = $datensatz[3];
                       }
                       else
                           {
                           $ThemaText = $datensatz[4];
                           }
           echo '<TD VALIGN="Top" class="marktabletd" WIDTH="90%"><B>'.$ThemaText.'</B></TD>';

           $MarkMe = "";                            # setze Flaggen
           if ($datensatz[2] != "") {$MarkMe = '<IMG SRC="grfx/flag_d.gif" WIDTH="16" HEIGHT="10">';}
           echo '<TD VALIGN="Middle" ALIGN="Center" class="marktabletd" WIDTH="16">'.$MarkMe.'</TD>'."\n";
           $MarkMe = "";
           if ($datensatz[3] != "") {$MarkMe = '<IMG SRC="grfx/flag_e.gif" WIDTH="16" HEIGHT="10">';}
           echo '<TD VALIGN="Middle" ALIGN="Center" class="marktabletd" WIDTH="16">'.$MarkMe.'</TD>'."\n";
           $MarkMe = "";
           if ($datensatz[4] != "") {$MarkMe = '<IMG SRC="grfx/flag_f.gif" WIDTH="16" HEIGHT="10">';}
           echo '<TD VALIGN="Middle" ALIGN="Center" class="marktabletd" WIDTH="16">'.$MarkMe.'</TD>'."\n";

           echo '<FORM ACTION METHOD="POST" ACTION="faq.php"><TD VALIGN="Middle" ALIGN="Center" class="marktabletd"><input type="hidden" name="VarID" value="'.$datensatz[0].'"><input type="hidden" name="do" value="edit"><input type="submit" value="edit" class="EditBtn"></input></TD></FORM>'."\n";
           echo '<TD width="1" class="marktabletd">&nbsp;</TD>'."\n";
           echo '<FORM ACTION METHOD="POST" ACTION="faq.php"><TD VALIGN="Middle" ALIGN="Center" class="marktabletd"><input type="hidden" name="VarID" value="'.$datensatz[0].'"><input type="hidden" name="do" value="delete"><input type="submit" value="DEL" class="DelBtn" onClick="return confirm(\'[ACHTUNG] Soll das Thema\n\n unwiderruflich gelöscht werden ?\')"></input></TD></FORM></TR>'."\n";
           echo '</TR>';
           $count ++;
           $PrevID = $datensatz[1];

           ### Antworten:
           $Acount = 1;
           $GetASql = mysql_query("SELECT * FROM faq WHERE Art='antwort' && TFaqID='$ThisTID' ORDER BY Antw_Sort");
           $Antworten = mysql_num_rows($GetASql);
           while ($Antwort = mysql_fetch_row($GetASql))
                  {
                  echo '<TR>';
                  echo '<TD VALIGN="Top" class="tabletd"> </TD>';
                  $MyUp = '<IMG SRC="grfx/x.gif" border="0" width="11" height="11"></a>&nbsp;&nbsp;';
                  $ThisTID = $datensatz[0];
                  if ($Acount != 1)                         # pos-verschieben Button..?
                      {
                      $ProID  = md5 (uniqid (rand()));
                      $MyUp = '<a href="faq.php?do=upA&id='.$Antwort[6].'&previd='.$APrevID.'&mo='.$ProID.'"><IMG SRC="grfx/p_up.gif" border="0" width="11" height="11"></a>&nbsp;&nbsp;';
                      }
                  if ($Antwort[9] != "")                 # Eine der DREI Antworten wird doch wohl ausgefuellt sein..? :)
                      {
                      $ThemaText = $Antwort[9];
                      }
                      elseif ($Antwort[10] != "")
                              {
                              $ThemaText = $Antwort[10];
                              }
                              else
                              {
                              $ThemaText = $Antwort[11];
                              }
                  echo '<TD VALIGN="Top" class="tabletd">'.$MyUp.$ThemaText.'</TD>';

                  $MarkMe = "";                            # setze Flaggen
                  if ($Antwort[9] != "") {$MarkMe = '<IMG SRC="grfx/flag_d.gif" WIDTH="16" HEIGHT="10">';}
                  echo '<TD VALIGN="Middle" ALIGN="Center" class="tabletd" WIDTH="16">'.$MarkMe.'</TD>'."\n";
                  $MarkMe = "";
                  if ($Antwort[10] != "") {$MarkMe = '<IMG SRC="grfx/flag_e.gif" WIDTH="16" HEIGHT="10">';}
                  echo '<TD VALIGN="Middle" ALIGN="Center" class="tabletd" WIDTH="16">'.$MarkMe.'</TD>'."\n";
                  $MarkMe = "";
                  if ($Antwort[11] != "") {$MarkMe = '<IMG SRC="grfx/flag_f.gif" WIDTH="16" HEIGHT="10">';}
                  echo '<TD VALIGN="Middle" ALIGN="Center" class="tabletd" WIDTH="16">'.$MarkMe.'</TD>'."\n";

                  echo '<FORM ACTION METHOD="POST" ACTION="faq.php"><TD VALIGN="Middle" ALIGN="Center" class="tabletd"><input type="hidden" name="VarID" value="'.$Antwort[0].'"><input type="hidden" name="do" value="Aedit"><input type="submit" value="edit" class="EditBtn"></input></TD></FORM>'."\n";
                  echo '<TD width="1" class="marktabletd">&nbsp;</TD>'."\n";
                  echo '<FORM ACTION METHOD="POST" ACTION="faq.php"><TD VALIGN="Middle" ALIGN="Center" class="tabletd"><input type="hidden" name="VarID" value="'.$Antwort[0].'"><input type="hidden" name="do" value="delete"><input type="submit" value="DEL" class="DelBtn" onClick="return confirm(\'[ACHTUNG] Soll die Antwort\n\n unwiderruflich gelöscht werden ?\')"></input></TD></FORM></TR>'."\n";
                  echo '</TR>';
                  $Acount ++;
                  $APrevID = $Antwort[6];
                  }
           ?>
           <TR>
           <TD VALIGN="Top" class="marktabletd2"> </TD>
           <TD VALIGN="Top" class="marktabletd2" colspan="7" align="center"><a href="faqn.php?do=NeueAntwort&id=<? echo $datensatz[0]; ?>"><B>[Frage/Antwort zuf&uuml;gen]</B></a></TD>
           </TR>
           <?
           }
    ?><TR>
    <TD VALIGN="Top" bgcolor="#aaaaaa" colspan="8" height="1"><img src="grfx/x.gif" width="1" height="1"></TD>
    </TR>
    <TR>
    <TD VALIGN="Top" class="marktabletd" colspan="8" align="center"> <BR>&nbsp;&nbsp;&nbsp;&nbsp;<a href="faqn.php?do=NeuesThema"><B>[Neues Thema anlegen]</B></a> <BR><BR> </TD>
    </TR><?

    echo '</TABLE>';
    echo '<br>&nbsp;&nbsp;(<B><IMG SRC="grfx/p_up.gif" border="0" width="11" height="11"> = </B>Thema bzw. Antwort in der Reihenfolge um eine Position nach oben verschieben)';
}

#______________________________________________________________________  ####### Input neues Thema

function EditThema ($XOrg,$EditID) {
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

#______________________________________________________________________  ####### Input neues Thema

function EditAntwort ($XOrg,$EditID) {
    ?>
    <TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" WIDTH="100%" class="outertablebg">
    <TR>
    <TD><IMG SRC="grfx/flag_d.gif">&nbsp;&nbsp;&nbsp;<span class="head"><B>Deutsch</B></span></TD></TR>
    <TR>
    </TABLE>
    <TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" class="outertablebg">
    <TR>
    <TD VALIGN="Top" class="tabletd">&nbsp; <BR><B>Frage:</B></TD>
    <TD VALIGN="Top" class="tabletd">&nbsp;&nbsp;&nbsp;</TD>
    <TD VALIGN="Top" class="tabletd">&nbsp; <BR><textarea name="FName_d" rows="4" cols="80"><? echo $XOrg[9]; ?></textarea></TD>
    </TR><TR>
    <TD VALIGN="Top" class="tabletd"><B>Antwort:</B></TD>
    <TD VALIGN="Top" class="tabletd">&nbsp;&nbsp;&nbsp;</TD>
    <TD VALIGN="Top" class="tabletd"><textarea name="RName_d" rows="4" cols="80"><? echo $XOrg[2]; ?></textarea>&nbsp; <BR> </TD>
    </TR></TABLE>
    <TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" WIDTH="100%" class="outertablebg">
    <TR>
    <TD><IMG SRC="grfx/flag_e.gif">&nbsp;&nbsp;&nbsp;<span class="head"><B>English</B></span></TD></TR>
    <TR>
    </TABLE>
    <TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" class="outertablebg">
    <TR>
    <TD VALIGN="Top" class="tabletd">&nbsp; <BR><B>Frage:</B></TD>
    <TD VALIGN="Top" class="tabletd">&nbsp;&nbsp;&nbsp;</TD>
    <TD VALIGN="Top" class="tabletd">&nbsp; <BR><textarea name="FName_e" rows="4" cols="80"><? echo $XOrg[10]; ?></textarea></TD>
    </TR><TR>
    <TD VALIGN="Top" class="tabletd"><B>Antwort:</B></TD>
    <TD VALIGN="Top" class="tabletd">&nbsp;&nbsp;&nbsp;</TD>
    <TD VALIGN="Top" class="tabletd"><textarea name="RName_e" rows="4" cols="80"><? echo $XOrg[3]; ?></textarea>&nbsp; <BR> </TD>
    </TR></TABLE>
    <TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" WIDTH="100%" class="outertablebg">
    <TR>
    <TD><IMG SRC="grfx/flag_f.gif">&nbsp;&nbsp;&nbsp;<span class="head"><B>Français</B></span></TD></TR>
    <TR>
    </TABLE>
    <TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" class="outertablebg">
    <TR>
    <TD VALIGN="Top" class="tabletd">&nbsp; <BR><B>Frage:</B></TD>
    <TD VALIGN="Top" class="tabletd">&nbsp;&nbsp;&nbsp;</TD>
    <TD VALIGN="Top" class="tabletd">&nbsp; <BR><textarea name="FName_f" rows="4" cols="80"><? echo $XOrg[11]; ?></textarea></TD>
    </TR><TR>
    <TD VALIGN="Top" class="tabletd"><B>Antwort:</B></TD>
    <TD VALIGN="Top" class="tabletd">&nbsp;&nbsp;&nbsp;</TD>
    <TD VALIGN="Top" class="tabletd"><textarea name="RName_f" rows="4" cols="80"><? echo $XOrg[4]; ?></textarea>&nbsp; <BR> </TD>
    </TR></TABLE>
    <HR SIZE="1">
    <?
}


#______________________________________________________________________  ####### ReLoad Browser / um moegliches cache-problem zu umgehen..

function ReRun ($DebugMode,$Message) {
   $ProID  = md5 (uniqid (rand()));
   $ReTime = 0;
   if ($DebugMode == "1") {echo '<B>'.$Message.'</B>&nbsp;&nbsp;&nbsp;&nbsp;<a href="faq.php?do=x&'.$ProID.'">(weiter)</a><HR SIZE="1"><P>'; $ReTime = 2; }
   echo '<META HTTP-EQUIV="Refresh" CONTENT="'.$ReTime.'; URL=faq.php?do=x&'.$ProID.'">';
}