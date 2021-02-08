<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2//EN">
<HTML>
<HEAD>
<link rel=stylesheet type=text/css href=../global/site.css>
</HEAD>
<BODY>

<?
require_once ("../bpadmin/settings.inc.php");
$db=mysql_connect($dbhost,$dbuser,$dbpass);                                     # open db
mysql_select_db($dbname);

echo '<TABLE BORDER="0" CELLSPACING="1" CELLPADDING="3" class="outertablebg">';
echo '<TR><TD COLSPAN="4" class="marktabletd2"><span class="phatt"><B>Downloads bp2k6-Homepage</B></span></TD></TR>';
$GetSql = mysql_query("SELECT * FROM download ORDER BY dl desc");
while ($datensatz = mysql_fetch_row($GetSql))
       {
       $FName = explode(".", $datensatz[0]);
       $TStamp = explode(" ", $datensatz[2]);
       $ODate = explode("-", $TStamp[0]);
       $MyDate =  $ODate[2].".".$ODate[1].".".$ODate[0].",";
       echo "<TR><TD VALIGN='Top' class='tabletd'><B>".$FName[0]."</B>.".$FName[1]."</TD>";
       echo "<TD VALIGN='Top' align='Right' class='tabletd'><B>".$datensatz[1]."</B></TD>";
       echo "<TD VALIGN='Top' align='Right' class='tabletd'>last: ".$MyDate.$TStamp[1]."</TD><TD class='tabletd'> &nbsp;</TD></TR>";
       $total = $total + $datensatz[1];
       }
?>
<TR>
<TD ALIGN="Right" class='tabletd'><b>TOTAL</b>:</TD>
<TD ALIGN="Right" class='tabletd'><B><? echo $total; ?></B></TD>
<TD COLSPAN="2" class='tabletd'> &nbsp;</TD>
</TR>
<?

echo '<TR><TD COLSPAN="4" class="marktabletd2"><span class="phatt"><B>Referer:</B></span></TD></TR>';
$GetSqlb = mysql_query("SELECT * FROM download_ext ORDER BY dl desc");
while ($datensatz = mysql_fetch_row($GetSqlb))
       {
       $RefLink = "";
       if ($datensatz[0] == "")
           {
           $RefLink = "[kein Referer]";
           }else
                {
                $RefLink = "<a href=\"$datensatz[0]\" target=_blank>".$datensatz[0]."</a>";
                }
       echo "<TR><TD VALIGN='Top' class='tabletd' colspan=3><B>".$RefLink."</b></TD>";
       echo "<TD class='tabletd'><B>".$datensatz[1]."</B></TD></TR>";
       }

echo '</TABLE>';

mysql_close($db);
?>

</BODY>
</HTML>


<?
# -----  Funktionen   ##########################################################


?>