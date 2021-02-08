<?

#### Heads/links ausgeben:
$GetSql = mysql_query("SELECT * FROM faq WHERE Art='thema' ORDER BY Sort");
while ($datensatz = mysql_fetch_row($GetSql))
        {
        if ($datensatz[2+$ThisLanguage]) {
        echo '<B>'.$datensatz[2+$ThisLanguage].'</B>';
        echo '<UL>';
        $ThisTID = $datensatz[0];
        $GetASql = mysql_query("SELECT * FROM faq WHERE Art='antwort' && TFaqID='$ThisTID' ORDER BY Antw_Sort");
        $Antworten = mysql_num_rows($GetASql);
        while ($Antwort = mysql_fetch_row($GetASql))
               {
               if ($Antwort[9+$ThisLanguage]) {
                   echo '<LI><a href="?id='.$Antwort[6].'#'.$Antwort[6].'">'.$Antwort[9+$ThisLanguage].'</a>';
                   }
               }
        echo '</UL>';
        }
      }


#### FAQs ausgeben:
$GetSql = mysql_query("SELECT * FROM faq WHERE Art='thema' ORDER BY Sort");
while ($datensatz = mysql_fetch_row($GetSql))
      {
      if ($datensatz[2+$ThisLanguage]) {
            echo '<BR><BR><BR><BR><span class="head"><LI><B>'.$datensatz[2+$ThisLanguage].'</B></span><P><HR SIZE="1">';
            $ThisTID = $datensatz[0];
            $GetASql = mysql_query("SELECT * FROM faq WHERE Art='antwort' && TFaqID='$ThisTID' ORDER BY Antw_Sort");
            $Antworten = mysql_num_rows($GetASql);
            $HlID = $_GET["id"];
            while ($Antwort = mysql_fetch_row($GetASql))
                   {
                   if ($Antwort[9+$ThisLanguage]) {
                                      echo '<P><a name="'.$Antwort[6].'"></a>';
                                      if ($Antwort[6] == $HlID)
                                          {
                                          $markme = "tabletd";
                                          }
                                          else
                                              {
                                              $markme = "marktabletd2";
                                              }
                                      echo '<TABLE BORDER="0" CELLSPACING="1" CELLPADDING="2" class="outertablebg" width="100%">';
                                      echo '<TR><TD class="'.$markme.'" WIDTH="99%">';
                                      echo '<B>'.$Antwort[9+$ThisLanguage].'</B><p>';
                                      echo ''.nl2br($Antwort[2+$ThisLanguage]).'<P>';
                                      echo '</TD>';
                                      echo '<TD class="marktabletd2" VALIGN="Top" WIDTH="10"><a href="#pagetop"><img src="../global/grafik/back.gif" border="0" hspace="5"></a></TD></TR>';
                                      echo '</TABLE>';
                                      }
                   }

            }
      }
echo "<BR><BR><BR><BR><BR>";

?>