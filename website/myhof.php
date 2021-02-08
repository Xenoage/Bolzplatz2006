<html>
<head>
<link rel=stylesheet type=text/css href=global/site.css>

<?
$Server = "http://www.xenoage.com/bp2k6";
$KillLang = $_GET["lang"];
$MyLanguage = strtolower($KillLang);
$gamesubmit = $_SERVER['QUERY_STRING'];
$replace = "&lang=$KillLang";
$MyLink = str_replace("$replace", "", $gamesubmit);

if ($MyLanguage == "de")
    {
    $ReDirect = $Server."/de/myhof.php?".$MyLink;
    echo '<META HTTP-EQUIV="Refresh" CONTENT="1; URL='.$ReDirect.'">';
    }
   elseif ($MyLanguage == "fr")
          {
          $ReDirect = $Server."/fr/myhof.php?".$MyLink;
          echo '<META HTTP-EQUIV="Refresh" CONTENT="1; URL='.$ReDirect.'">';
          }
     else
          {
          $ReDirect = $Server."/en/myhof.php?".$MyLink;
          echo '<META HTTP-EQUIV="Refresh" CONTENT="1; URL='.$ReDirect.'">';
          }
?>

</head>
<body bgcolor="#ffffff" text="#666666" link="#666666" alink="#666666" vlink="#666666">

<CENTER>
<TT><BR><BR>
redirect/start hall of fame<P>
<a href="<? echo $ReDirect; ?>">[or click here]</a>
</TT>
</CENTER>

</body>
</html>