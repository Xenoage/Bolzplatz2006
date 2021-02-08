<html>
<head></head>
<body bgcolor=#ffffff>

<?

# --------------------------------------------------------------------#
$ScriptURL = "de/myhof.php";
# --------------------------------------------------------------------#


$MyTimeCode = time ();
$ThisDay = date("z",time());

$donow = $_GET["do"];
if ($donow == "view" && $team_id != "")   # wenn ausgefuellt - dann zeigen ,)
    {
    $team_id = str_replace(" ", "_", $team_id);
    $team_id = strtolower($team_id);
    $Debug = "1";
    }


?>
<CENTER><TABLE BORDER=6 bgcolor=#f0f0f0 cellpadding=7 width=700 height=95%><TR><TD valign=top>
<TT>
<B>ReBuild HoF</B> <HR SIZE="1">
<FORM ACTION="?do=view" METHOD="POST">
<B>Userangaben:</B><BR>
teamid:&nbsp;&nbsp;&nbsp;
<INPUT TYPE="text" NAME="teamid" SIZE="40" MAXLENGTH="80" VALUE="<? echo $_POST["teamid"]; ?>"><BR>
gamemode:&nbsp;
<?
if (($_POST["gamemode"] == "") or ($_POST["gamemode"] == "career"))
     {
     $FlCr = "checked";
     $Flwc = "";
     } else {
            $FlCr = "";
            $Flwc = "checked";
            }
?>
<input type="radio" name="gamemode" value="career" <? echo $FlCr; ?>> - career&nbsp;&nbsp;
<input type="radio" name="gamemode" value="worldcup" <? echo $Flwc; ?>> - worldcup<BR>
gamecode:&nbsp;
<INPUT TYPE="text" NAME="gamecode" SIZE="40" MAXLENGTH="80" VALUE="<? echo $_POST["gamecode"]; ?>"><BR>
int:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<INPUT TYPE="text" NAME="int" SIZE="40" MAXLENGTH="80" VALUE="<? echo $_POST["int"]; ?>"><BR>


<P>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<INPUT TYPE="submit" VALUE="ReBuild :)">
</FORM><HR SIZE=1><BR>
<?

if ($_POST["teamid"]){

$MyTimeCode = time ();
$ThisDay = date("z",time());
echo '<CENTER><TABLE BORDER="0" CELLPADDING="4" WIDTH="400">';
echo "<TR><TD colspan=2><TT><B>Server</B></TD></TR>";
echo "<TR><TD><TT>Timestamp:</TD><TD><TT>$MyTimeCode</TD></TR>";
echo "<TR><TD><TT>Tag:</TD><TD><TT><b>$ThisDay</b></TD></TR><TR><TD colspan=2>&nbsp; </TD></TR>";

$team_id = $_POST["teamid"];
$code = $_POST["gamecode"];
$game_mode = $_POST["gamemode"];
$game_ts = ($_POST["int"]) / 214;
$OrigCode = $code;

## reverse code
for ($i = 0; $i < (strlen($team_id)); $i++)
     {
     $ThisAscii = ord($team_id{$i});
     $code = $code - 5479 * ($i + 9973) * $ThisAscii;
     }
if ($game_mode == "career")
   {
   $code = $code / 3;
   }
   else
           {
           $code = $code / 5;
           }
## end reverse code

echo "<TR><TD colspan=2><B><TT>User int</B>&nbsp;&nbsp;( = OrigTimest. User PC)</TD></TR>";
echo "<TR><TD><TT>Timestamp:</TD><TD><TT>$game_ts</TD></TR>";
$GameDay = date("z",$game_ts);
echo "<TR><TD><TT>Tag:</TD><TD><TT>$GameDay</TD></TR><TR><TD colspan=2>&nbsp; </TD></TR>";

echo "<TR><TD colspan=2><B><TT>Check GameCode:</B>&nbsp;&nbsp;($OrigCode)</TD></TR>";

$RebGameDay = date("z",$code);
if ($RebGameDay < 1) {$RebGameDay = "unknowm";}
echo "<TR><TD><TT>Timestamp:</TD><TD><TT>$code</TD></TR>";
echo "<TR><TD><TT>Tag:</TD><TD><TT><b>$RebGameDay</b></TD></TR></TR><TR><TD colspan=2>&nbsp; </TD></TR>";
$Abweichung = $ThisDay - date("z",$code);
echo "<TR><TD colspan=2><B><TT>Abweichung:</B>&nbsp;&nbsp;$Abweichung Tage</TD></TR>";

echo '</TABLE></CENTER>';

}
?>
</TD></TR></TABLE>
</body>

</html>