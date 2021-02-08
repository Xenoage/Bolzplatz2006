<?

$ViewMode = $_GET["debug"];
if ($ViewMode == "on")
    {
    $Debug = 1;
    }
if ($game_mode != "career" && $game_mode != "worldcup")   ## wurde dran rumgefummelt..?
    {
    $Fatal = 1;
    // Fatal ();
    // die;
    }
if (($Debug) and ($Fatal != "1"))
    {
    echo '<TABLE border=0 cellspacing=0 cellspacing=0>';
    echo '<TR><TD><TT>TIMESTAMP SERVER:&nbsp; <BR> &nbsp;</TD><TD><TT>'.$MyTime.' = <B>DAY: '.$ThisDay.'&nbsp; <BR> &nbsp;</TD></TR>';
    echo '<TR><TD><TT>EMPFANGEN CODE: </TD><TD><TT>'.$code.'</TD></TR>';
    echo '<TR><TD><TT>EMPFANGEN GAMEMODE: </TD><TD><TT>'.$game_mode.' = '.strlen($game_mode).' ZEICHEN</TD></TR>';
    echo '<TR><TD><TT>EMPFANGEN TEAMID: </TD><TD><TT>'.$team_id.' = '.strlen($team_id).' ZEICHEN</TD></TR>';
    echo '</TABLE><BR><TT>STARTE '.strlen($team_id).' DURCHLAEUFE x ASCII:<BR>';
    }
## reverse code
for ($i = 0; $i < (strlen($team_id)); $i++)
     {
     $ThisAscii = ord($team_id{$i});
     $code = $code - 5479 * ($i + 9973) * $ThisAscii;
     if (($Debug) and ($Fatal != "1")) {echo '<B>'.$team_id{$i}."</B> = ".ord($team_id{$i}).'<BR>';}
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
if (($Debug) and ($Fatal != "1"))
    {
    echo '<P><TABLE border=0 cellspacing=0 cellspacing=0>';
    echo '<TR><TD><TT>REVERSE CODE (GameTimeStamp): </TD><TD><TT>'.$code.' = <B>DAY: '.date("z",$code).'</TD></TR>';
    echo '</TABLE>';
    }
$GameDiff = $ThisDay - date("z",$code);
if ($bdcode == $bd) {$GameDiff = $MaxAbweichung;}
if ($GameDiff < 0) {$GameDiff = $GameDiff * -1;}

?>