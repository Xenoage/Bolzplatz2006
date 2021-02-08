<html>
<head></head>
<body bgcolor=#ffffff>

<?

# --------------------------------------------------------------------#
$ScriptURL = "http://www.xenoage.com/bp2k6/myhof.php";
# --------------------------------------------------------------------#


$code = time ();
$ThisDay = date("z",time());

if ($_POST["game_id_manual"])
    {
    $team_id = $_POST["game_id_manual"];
    }
    else {
         $team_id = $_POST["game_id_auto"];
         }

$donow = $_GET["do"];
if ($donow == "view" && $team_id != "")   # wenn ausgefuellt - dann zeigen ,)
    {
    $team_id = str_replace(" ", "_", $team_id);
    $team_id = strtolower($team_id);
    $Debug = 1;
    }


?>
<CENTER><TABLE BORDER=6 bgcolor=#f0f0f0 cellpadding=7 width=700 height=95%><TR><TD valign=top>
<TT>
<B>Build HoF</B> &nbsp;&nbsp;&nbsp;<I>WHAT a CHEAT!</I><HR SIZE="1">

<? if ($donow != "view" && $team_id == "") { ?>
<FORM ACTION="?do=view" METHOD="POST">
TEAMID:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<SELECT NAME="game_id_auto">
<option value="">- - - - - - - - -
<?
require_once ("settings.inc.php");
$db=mysql_connect($dbhost,$dbuser,$dbpass);                                     # open db
mysql_select_db($dbname);
$GetSql = mysql_query("SELECT * FROM hof_teams WHERE Mode='career' ORDER BY TeamName");
while ($datensatz = mysql_fetch_row($GetSql))
       {
        echo "<option value=\"$datensatz[0],$datensatz[1]\">$datensatz[2]";
       }
$GetSql = mysql_query("SELECT * FROM hof_teams_wc WHERE Mode='worldcup' ORDER BY TeamName");
while ($datensatz = mysql_fetch_row($GetSql))
       {
        echo "<option value=\"$datensatz[0],$datensatz[1]\">$datensatz[2]";
       }
mysql_close($db);
?>
</SELECT>

<BR>
EIGENE:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<INPUT TYPE="text" NAME="game_id_manual" SIZE="40" MAXLENGTH="80" VALUE=""><BR>
Link für:&nbsp;&nbsp;&nbsp;
<SELECT NAME="lang">
<option value="de">DE Homepage
<option value="en">EN Homepage
<option value="fr">FR Homepage
</SELECT>
<P>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<INPUT TYPE="submit" VALUE="Link erzeugen">
</FORM><HR SIZE=1><BR>
<?
}

$idsubmit = explode(",", $team_id);
$team_id = $idsubmit[0];
if ($idsubmit[1])
    {
    $game_mode = $idsubmit[1];
    }
    else {
         $game_mode = "career";
         }

if (($Debug) and ($Fatal != "1"))
    {
    echo '<TABLE border=0 cellspacing=0 cellspacing=0>';
    echo '<TR><TD><TT>TIMESTAMP SERVER:&nbsp; <BR> &nbsp;</TD><TD><TT>'.$code.' = <B>DAY: '.$ThisDay.'&nbsp; <BR> &nbsp;</TD></TR>';
    echo '<TR><TD><TT>TEAMID: </TD><TD><TT><B>'.$team_id.'</B> = '.strlen($team_id).' ZEICHEN</TD></TR>';
    echo '<TR><TD><TT>GAMEMODE: </TD><TD><TT><B>'.$game_mode.'</B> = '.strlen($game_mode).' ZEICHEN</TD></TR>';
    echo '</TABLE><BR><TT>STARTE '.strlen($team_id).' DURCHLAEUFE x ASCII:<BR>';
    }


# MakeGameCode #################################################################
                                                                                #           #
if ($game_mode == "career")                                                      #           #
   {                                                                              #          ##
   $code = $code * 3;                                                              #
   }                                                                              #        ##   ##
   elseif ($game_mode == "worldcup")                                             #         #    #
           {                                                                     #
           $code = $code * 5;                                                     #          ##
           }                                                                     #
           else {                                                                #        ########
                $Fatal = 1;                                                       #       ##   ##
                }                                                                #          ###      ##
for ($i = 0; $i < (strlen($team_id)); $i++)                                     #            #     ##
     {                                                                           #    #############
     $ThisAscii = ord($team_id{$i});                                              #          #
     $code = $code + 5479 * ($i + 9973) * $ThisAscii;                              #         #
     if ($Debug) {echo '<B>'.$team_id{$i}."</B>=".ord($team_id{$i}).'<BR>';}      #         # #
     }                                                                           #         #   #
                                                                                #         #     #
# MakeGameCode-End #############################################################        ###     ###

if ($Debug)
    {
    echo "<P>Errechneter Code: <B>$code</B><P><P> ";
    echo "<a href=$ScriptURL?gamemode=$game_mode&team=$team_id&code=$code&lang=".$_POST["lang"]." target=_blank>Seite aufrufen</a><P><B>URL:</B><BR>$ScriptURL?gamemode=$game_mode&team=$team_id&code=$code&lang=".$_POST["lang"];
    ?>
    <FORM ACTION="build.php" METHOD="POST">
    <INPUT TYPE="submit" VALUE="zurück / neuer link">
    </FORM>
    <?
    }


?>
</TD></TR></TABLE>
</body>

</html>