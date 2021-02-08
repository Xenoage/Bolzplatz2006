<?
$GetSql = mysql_query("SELECT * FROM mods2_rubrik WHERE ID='$KatID'");
$XTemp = mysql_fetch_row($GetSql);
$HeadExt = $XTemp[$ThisLanguage+1];
?>
<span class="head"><B>Mods<? echo " / $HeadExt"; ?></B></span> <P><?

if ($Anzahl == 0)
    {
    echo '<CENTER><HR SIZE="1"><FONT SIZE="3"><TT><B>error:</B> not avaible / no entries, sorry :(</TT></FONT><HR SIZE="1"></CENTER>';
    $Datei = basename($_SERVER["PHP_SELF"]);
    echo "<META HTTP-EQUIV=\"Refresh\" CONTENT=\"2; URL=".$Datei."\">";
    }

$SortMe = $_GET["tsort"];
$az = "";
if ($SortMe != "author" and $SortMe != "date")
    {
    $SortMe = "name";
    }
if ($SortMe == "date")
    {
    $az = "desc";
    }

$team_result = mysql_query("SELECT * FROM mods2_data WHERE type='$KatID' ORDER BY $SortMe $az");
if (mysql_num_rows($team_result))
    {
?>
<span class="head"><B><?=$lang_headline?></B></span><P>
<?=$lang_text?>:
&nbsp;&nbsp;<? if ($SortMe == "name") {echo "<B>";} ?>
<a href="?view=<? echo $KatID ?>&tsort=name"><?=$lang_team?></a></B>
&nbsp;&nbsp;|&nbsp;&nbsp;<? if ($SortMe == "author") {echo "<B>";} ?>
<a href="?view=<? echo $KatID ?>&tsort=author"><?=$lang_user?></a></B>
&nbsp;&nbsp;|&nbsp;&nbsp;<? if ($SortMe == "date") {echo "<B>";} ?>
<a href="?view=<? echo $KatID ?>&tsort=date"><?=$lang_date?></a></B>
<P>

<TABLE BORDER="0" CELLSPACING="1" CELLPADDING="0" WIDTH="100%" class="outertablebg">
<?
$lr = 1;
while ($team = mysql_fetch_object($team_result))
{
$DBdate = $team->date;
$tmp = explode("-", $DBdate);
if ($lang == "de")
  $FDate = $tmp[2].".".$tmp[1].".".$tmp[0];
else
  $FDate = $tmp[0]."-".$tmp[1]."-".$tmp[2];

if ($lr == 1)
    {
    echo "<TR>";
    }
if ($lr > 2)
    {
    $lr = 1;
    echo "</TR>";
    }
?>
  <TD VALIGN="Middle" class="tabletd" WIDTH="65">
    <a href="../download/mods/<? echo $KatID ?>/<?=$team->file?>.zip"><IMG SRC="../global/grafik/mods/<? echo $KatID ?>/<?=$team->file?>.png" WIDTH="64" HEIGHT="64" hspace="5" vspace="1" border="0"></a></TD>
  <TD VALIGN="Middle" class="tabletd" WIDTH="50%">
    &nbsp;&nbsp;<span class="head"><B><?=$team->name?></B></span><br>
    &nbsp;&nbsp;<B><?=$lang_author?>: <?=$team->author?></B><BR>
    &nbsp;&nbsp;<?=$FDate?></B>
  </TD>
<?
$lr ++;
}
if ($lr == 2)    # sauberer Tabellenabschluss?
   {
   echo '<TD colspan="2" class="tabletd">&nbsp;</TD></TR>';
   }
$XTemp = mysql_fetch_row(mysql_query("SELECT * FROM bpmain"));
$Button = $XTemp[$ThisLanguage+8];
?>
</TABLE>
 <BR><div align="right"><B><a href="?"><? echo $Button ?> &gt;&gt;</B></a></div>

<?
  }
?>