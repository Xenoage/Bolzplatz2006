<?

    # ====================================================== #
    # BP2k6 - Homepage => News (CMS)                         #
    # ------------------------------------------------------ #
    # (c)2006 maus / m.schmitz / maussites.de                #
    # LastChange: 26.05.2006                                 #
    # ====================================================== #


# status / todo:
# ______________________________________________________________________________

$GetSql = mysql_query("SELECT * FROM bpmain WHERE Art='settings'");             # hole settings
$XTemp = mysql_fetch_row($GetSql);
$MaxChars = $XTemp[7];                     ## hole MaxChars fuer Zeichenbegrenzung
if (($ThisLanguage < 1)or($ThisLanguage >3))  # (ID-DAU..? :)
     {$ThisLanguage = "1";}
$WeiterBtn = $XTemp[8 + $ThisLanguage];    ## Hole Namen des [weiter]-Buttons
$LanguageID = $ThisLanguage * 2;
$Datei = basename($_SERVER["PHP_SELF"]);   ## Mein Dateiname

if ($_GET["id"] == "") {

$GetSql = mysql_query("SELECT * FROM news ORDER BY NewsID desc");               # hole alle News..
while ($data = mysql_fetch_row($GetSql))                                        # ..Ausgabe aller News
       {
       if ($data[$LanguageID] != "")
           {
           $teil = explode(", ", $data[1]);
           /*
           echo '<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2">';
           #echo '<TR><TD><B>'.$teil[0]." / ".$teil[1].':</B></TD></TR>';   # MIT Zeit-Ausgabe
           echo '<TR><TD class="marktabletd2">'.$teil[0]." / ".$data[8].':&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD></TR>';
           echo '</TABLE>';
           */
           echo '<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="3" class="outertablebg" WIDTH="100%">';
           echo '<TR><TD VALIGN="Top" class="marktabletd" colspan="2"><B>'.$data[$LanguageID].'</B></TD></TR>';

           $FullText = $data[$LanguageID+1];
           $MyAktText = "";
           if ($MaxChars != "0")
                {
                $tok = strtok($FullText, " ");
                $counter = 0;
                while (($tok !== false)and($counter != $MaxChars)) {
                        #echo "$tok _";
                        $MyAktText = $MyAktText.$tok." ";
                        $tok = strtok(" \n\t");
                        $counter ++;
                        }
                if ($counter >= $MaxChars)
                    {
                    $MyAktText = $MyAktText.'...<a href="'.$Datei.'?id='.$data[0].'"><B>('.$WeiterBtn.')</B></a>';
                    }
                }
                else {
                     $MyAktText = $FullText;
                     }

           $FullText = nl2br($MyAktText);
           echo '<TR><TD VALIGN="Top" class="marktabletd2">'.$FullText.'</TD>';
           if ($data[9] != "")
               {
               echo '<TD VALIGN="Top" ALIGN="Right" class="marktabletd2">';
               ?><a href="javascript:showPicture2('../global/grafik/news/<? echo $data[9]; ?>','-');"><?
               echo '<img src="../global/grafik/news/pv_';
               echo $data[9].'" border="1"><br>[ click ]</a></TD>';
               }
           echo '</TR>';
           echo '<TR><TD colspan="2" class="marktabletd" ALIGN="Left">('.$teil[0]." / ".$data[8].')</TD></TR>';
           echo '</TABLE><BR>';
           }
       }

   }
   else                                                                         # Ausgabe des gewaehlten Eintrages
       {
       $GetSql = mysql_query("SELECT * FROM news WHERE NewsID='$_GET[id]'");    # hole gewaehlte News
       $data = mysql_fetch_row($GetSql);
       if ($data[1] == "")                       # existiert der Eintrag..?
           {
           echo '<CENTER><HR SIZE="1"><FONT SIZE="3"><TT><B>error:</B> this article is not avaible, sorry :(</TT></FONT><HR SIZE="1"></CENTER>';
           echo "<META HTTP-EQUIV=\"Refresh\" CONTENT=\"2; URL=".$Datei."\">";
           }
           else {                                # naklar  :)
                 $teil = explode(", ", $data[1]);
                 echo '<TABLE BORDER="0" CELLSPACING="1" CELLPADDING="3" class="outertablebg" WIDTH="100%">';
                 echo '<TR><TD class="marktabletd2"><B><LI>'.$teil[0].", ".$teil[1]." /".$data[8].':</B></TD></TR>';
                 echo '<TR><TD class="marktabletd">';
                 echo '<B>'.$data[$LanguageID].'</B></TD></TR>';
                 echo '<TR><TD class="tabletd"><div align="justify">';
                 if ($data[9] != "")
                     {
                     ?><a href="javascript:showPicture2('../global/grafik/news/<? echo $data[9]; ?>','-');"><?
                     echo '<img src="../global/grafik/news/pv_';
                     echo $data[9].'" border="1" align="Right" hspace="15" vspace="10"></a>';
                     }
                 echo nl2br($data[$LanguageID+1]);
                 echo '</div></TD></TR>';
                 echo '<TR><TD class="marktabletd2" ALIGN="Right">';
                 echo '<a href="javascript:history.go(-1)" onMouseOver="self.status=document.referrer;return true"><B>'.$WeiterBtn.'</B></a>&nbsp;';
                 echo '</TD></TR></TABLE>';
                }
       }

?>