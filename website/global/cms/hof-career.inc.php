<?

if ($_GET["v"])
    {
    $ViewID = $_GET["v"];
    if ($ViewID != "usr")
        {
        $GetSql = mysql_query("SELECT * FROM hof_teams WHERE TeamID='$ViewID'");
        $datensatz = mysql_fetch_row($GetSql);
        if ($datensatz[0] == "" || $datensatz[3] < 1)
                {
                echo '<CENTER><HR SIZE="1"><FONT SIZE="3"><TT><B>error:</B> this team is not avaible, sorry :(</TT></FONT><HR SIZE="1"></CENTER>';
                echo "<META HTTP-EQUIV=\"Refresh\" CONTENT=\"2; URL=".$ThisFile."\">";
                }
                else {
                      ## Ausgabe teameintraege
                      echo '<TABLE BORDER="0" CELLSPACING="1" CELLPADDING="0" WIDTH="100%" class="outertablebg">';
                            $CheckPict = $datensatz[0]."-logo.gif";
                            if (is_file('../global/grafik/teamlogos/'.$CheckPict))
                                {
                                $ThisPict = $datensatz[0]."-logo.gif";
                                }
                                else {
                                      $ThisPict = "_none.gif";
                                      }
                      ?>
                      <TR>
                      <TD VALIGN="Middle" class="tabletd" WIDTH="100"><IMG SRC="../global/grafik/teamlogos/<? echo $ThisPict; ?>" WIDTH="65" HEIGHT="65" hspace="5" vspace="3" border="0"></TD>
                      <TD VALIGN="Middle" class="tabletd" WIDTH="95%">
                      &nbsp;&nbsp;<span class="head"><B><? echo $datensatz[2]; ?></B></span><br>
                      &nbsp;&nbsp;<B><? echo $htext01.$datensatz[3]; ?>
                      </B></TD>
                      </TR>
                      <TR><TD COLSPAN="2" HEIGHT="2" class="marktabletd2"><IMG SRC="../global/grafik/x.gif" WIDTH="2" HEIGHT="2"></TD></TR>
                      </TABLE>
                      <?
                      $GetUsrSql = mysql_query("SELECT * FROM hof_user WHERE TeamID='$ViewID' ORDER BY SubTstamp desc");
                      echo '<TABLE BORDER="0" CELLSPACING="1" CELLPADDING="2" WIDTH="100%" class="outertablebg">';
                      while ($MyUser = mysql_fetch_row($GetUsrSql))
                            {
                            if ($MyTblCol == "marktabletd") {$MyTblCol = "marktabletd2";} else {$MyTblCol = "marktabletd";}
                            echo "<TR><TD VALIGN=\"Middle\" class=\"$MyTblCol\" WIDTH=\"10\"><a href=\"$ThisFile"."?u=".$MyUser[4]."\"><IMG SRC=\"../global/grafik/flags/$MyUser[4].gif\" width=23 height=13 border=0 hspace=4></a></TD>";
                            echo "<TD VALIGN=\"Middle\" class=\"$MyTblCol\" WIDTH=\"33%\"><B>$MyUser[2]</B></TD>";
                            echo "<TD VALIGN=\"Middle\" class=\"$MyTblCol\" WIDTH=\"33%\"><B>$htext02 $MyUser[5]</B></TD>";
                            echo "<TD VALIGN=\"Middle\" class=\"$MyTblCol\" WIDTH=\"33%\"><B>$htext03 $MyUser[3]</B></TD></TR>";
                            }
                            echo '</TABLE>';
                      echo "<BR><BR><HR SIZE=\"1\"><B><a href=\"$ThisFile\"><B>&lt;&lt; $htext06</a></B><HR SIZE=\"1\">";
                      ## Ausgabe teameintraege -end
                      }
        }

        else {
             if ($AutomHoF == "0")
              {
              $GetSql = mysql_query("SELECT * FROM hof_teams WHERE Mode='own' && No!='0' ORDER BY tstamp desc");
              }
              else {
                   $GetSql = mysql_query("SELECT * FROM hof_teams WHERE Mode='own' && No!='0' && Offline='1' ORDER BY tstamp desc");
                   }
             #$datensatz = mysql_fetch_row($GetSql);
             $Anzahl = mysql_num_rows($GetSql);
             echo '<TABLE BORDER="0" CELLSPACING="1" CELLPADDING="0" WIDTH="100%" class="outertablebg">';
             ?>
             <TR>
             <TD VALIGN="Middle" class="tabletd" WIDTH="65"><IMG SRC="../global/grafik/teamlogos/_own.gif" WIDTH="65" HEIGHT="65" hspace="5" vspace="3" border="0">
             <TD VALIGN="Middle" class="tabletd" WIDTH="95%">
             &nbsp;&nbsp;<span class="head"><B><? echo $htext09; ?></B></span><br>
             &nbsp;&nbsp;<B><? echo $htext01.$Anzahl; ?>
             </TD></TR>
             </TABLE>
             <?
                      ## Ausgabe usereintraege
                      echo '<TABLE BORDER="0" CELLSPACING="1" CELLPADDING="2" WIDTH="100%" class="outertablebg">';
                      while ($GetUser = mysql_fetch_row($GetSql))
                            {
                            $OwnUser = $GetUser[0];
                            #echo $OwnUser."<BR>";
                            $GetUsrSql = mysql_query("SELECT * FROM hof_user WHERE TeamID='$OwnUser' ORDER BY SubTstamp desc");
                            $MyUser = mysql_fetch_row($GetUsrSql);
                            if ($MyTblCol == "marktabletd") {$MyTblCol = "marktabletd2";} else {$MyTblCol = "marktabletd";}
                            echo "<TR><TD VALIGN=\"Middle\" class=\"$MyTblCol\" WIDTH=\"10\"><a href=\"$ThisFile"."?u=".$MyUser[4]."\"><IMG SRC=\"../global/grafik/flags/$MyUser[4].gif\" width=23 height=13 border=0 hspace=4></a></TD>";
                            echo "<TD VALIGN=\"Middle\" class=\"$MyTblCol\"><B>$MyUser[2]</B></TD>";
                            echo "<TD VALIGN=\"Middle\" class=\"$MyTblCol\"><B>$htext02 $MyUser[5]</B></TD>";
                            echo "<TD VALIGN=\"Middle\" class=\"$MyTblCol\"><B>$htext03 $MyUser[3]</B></TD>";
                            if ($ShowOwnTeamName == 1)
                                 {
                                 echo "<TD VALIGN=\"Middle\" class=\"$MyTblCol\"><B>$htext10 $GetUser[2]</B></TD>";
                                 }
                            echo '</TR>';
                            }
                            echo '</TABLE>';
                      echo "<BR><BR><HR SIZE=\"1\"><B><a href=\"$ThisFile\"><B>&lt;&lt; $htext06</a></B><HR SIZE=\"1\">";
                      ## Ausgabe usereintraege -end
             }

    }

      elseif ($_GET["u"])
             {
             require_once ('../global/cms/hof-userlands.inc.php');
             }

    else {
         ## Ausgabe uebersicht
         echo '<TABLE BORDER="0" CELLSPACING="1" CELLPADDING="0" WIDTH="100%" class="outertablebg">';
         $GetSql = mysql_query("SELECT * FROM hof_teams WHERE Mode='career' ORDER BY No desc");
         while ($datensatz = mysql_fetch_row($GetSql))
                {
                if ($datensatz[3] > 0)                                                     # eintraege vorhanden?
                    {
                    if (($AutomHoF == "0") or ($AutomHoF == "1" && $datensatz[4] == "0"))  # leer bzw. blocken?
                         {
         $cnt ++;
         $CheckPict = $datensatz[0]."-logo.gif";
         if (is_file('../global/grafik/teamlogos/'.$CheckPict))
             {
             $ThisPict = $datensatz[0]."-logo.gif";
             }
             else {
                  $ThisPict = "_none.gif";
                  }
         ?>
         <TR>
         <TD VALIGN="Middle" class="tabletd" WIDTH="65"><a href="<? echo $ThisFile."?v=".$datensatz[0]; ?>"><IMG SRC="../global/grafik/teamlogos/<? echo $ThisPict; ?>" WIDTH="65" HEIGHT="65" hspace="5" vspace="3" border="0"></a></TD>
         <TD VALIGN="Middle" class="tabletd" WIDTH="95%">
         &nbsp;&nbsp;<span class="head"><B><? echo $datensatz[2]; ?></B></span><br>
         &nbsp;&nbsp;<? echo $htext01.$datensatz[3]." - ".$htext05 ?>
         <B> <? echo $datensatz[5]."  ".$htext04.$datensatz[6]; ?></B><br>
         &nbsp;&nbsp;<a href="<? echo $ThisFile."?v=".$datensatz[0]; ?>"><B><? echo $htext07; ?></B></a>&nbsp;&nbsp;</div>
         </TD>
         </TR>
         <TR><TD COLSPAN="2" HEIGHT="2" class="marktabletd2"><IMG SRC="../global/grafik/x.gif" WIDTH="2" HEIGHT="2"></TD></TR>
         <?
                         }
                    }
                }
         echo '</TABLE>';

         if ($AutomHoF == "0")
              {
              $GetSql = mysql_query("SELECT * FROM hof_teams WHERE Mode='own' && No!='0' ORDER BY tstamp desc");
              }
              else {
                   $GetSql = mysql_query("SELECT * FROM hof_teams WHERE Mode='own' && No!='0' && Offline='1' ORDER BY tstamp desc");
                   }

         $datensatz = mysql_fetch_row($GetSql);
         $Anzahl = mysql_num_rows($GetSql);
         if ($Anzahl > 0) {
         echo '<BR><BR><TABLE BORDER="0" CELLSPACING="1" CELLPADDING="0" WIDTH="100%" class="outertablebg">';
         ?>
         <TR>
         <TD VALIGN="Middle" class="tabletd" WIDTH="65"><a href="<? echo $ThisFile."?v=".usr; ?>"><IMG SRC="../global/grafik/teamlogos/_own.gif" WIDTH="65" HEIGHT="65" hspace="5" vspace="3" border="0"></a>
         <TD VALIGN="Middle" class="tabletd" WIDTH="95%">
         &nbsp;&nbsp;<span class="head"><B><? echo $htext09; ?></B></span><br>
         &nbsp;&nbsp;<? echo $htext01.$Anzahl." - ".$htext05 ?>
         <B> <? echo $datensatz[5]."  ".$htext04.$datensatz[6]; ?></B><br>
         &nbsp;&nbsp;<a href="<? echo $ThisFile."?v=".usr; ?>"><B><? echo $htext07; ?></B></a>&nbsp;&nbsp;</div>
         </TD>
         </TR>
         <?

         echo '</TABLE>';
         }


         if ($cnt == "")
             {
             echo "<BR><BR><UL><LI><B>$htext08</B></UL>";
             }
         ## Ausgabe uebersicht -end
         }

?>
