<?

$GetLang = $_GET["u"];

             $GetUsrSql = mysql_query("SELECT * FROM hof_user_wc WHERE Land='$GetLang' ORDER BY SubTstamp desc");
             if (mysql_num_rows($GetUsrSql) > 0)
                          {
                          echo "<P><B>".$htext12."</B>";
                          echo "<IMG SRC=\"../global/grafik/flags/$GetLang.gif\" width=23 height=13 border=0 hspace=4> / <b>$htext20:</b><P>";
                          echo '<P><TABLE BORDER="0" CELLSPACING="1" CELLPADDING="2" WIDTH="100%" class="outertablebg">';
                          while ($MyUser = mysql_fetch_row($GetUsrSql))
                            {
                            if ($MyTblCol == "marktabletd") {$MyTblCol = "marktabletd2";} else {$MyTblCol = "marktabletd";}
                            echo "<TR>";
                            echo "<TD VALIGN=\"Middle\" class=\"$MyTblCol\" WIDTH=\"25%\"><B>$MyUser[2]</B></TD>";
                            echo "<TD VALIGN=\"Middle\" class=\"$MyTblCol\" WIDTH=\"25%\">$htext03 $MyUser[3]</TD>";
                            echo "<TD VALIGN=\"Middle\" class=\"$MyTblCol\" WIDTH=\"50%\">$htext02 $MyUser[5]</TD>";
                            echo "<TD VALIGN=\"Middle\" class=\"$MyTblCol\" WIDTH=\"10\"><IMG SRC=\"../global/grafik/flags/$MyUser[4].gif\" width=23 height=13 border=0 hspace=4></TD>";
                            echo "</TR>";
                            }
                          echo '</TABLE>';
                          }

             $GetUsrSql = mysql_query("SELECT * FROM hof_user WHERE Land='$GetLang' ORDER BY SubTstamp desc");
             if (mysql_num_rows($GetUsrSql) > 0)
                          {
                          echo "<BR><B>".$htext12."</B>";
                          echo "<IMG SRC=\"../global/grafik/flags/$GetLang.gif\" width=23 height=13 border=0 hspace=4> / <b>$htext21:</b><P>";
                          echo '<TABLE BORDER="0" CELLSPACING="1" CELLPADDING="2" WIDTH="100%" class="outertablebg">';
                          while ($MyUser = mysql_fetch_row($GetUsrSql))
                            {
                            if ($MyTblCol == "marktabletd") {$MyTblCol = "marktabletd2";} else {$MyTblCol = "marktabletd";}
                            echo "<TR>";
                            echo "<TD VALIGN=\"Middle\" class=\"$MyTblCol\" WIDTH=\"25%\"><B>$MyUser[2]</B></TD>";
                            echo "<TD VALIGN=\"Middle\" class=\"$MyTblCol\" WIDTH=\"25%\">$htext03 $MyUser[3]</TD>";
                            echo "<TD VALIGN=\"Middle\" class=\"$MyTblCol\" WIDTH=\"50%\">$htext02 $MyUser[5]</TD>";
                            echo "<TD VALIGN=\"Middle\" class=\"$MyTblCol\" WIDTH=\"10\"><IMG SRC=\"../global/grafik/flags/$MyUser[4].gif\" width=23 height=13 border=0 hspace=4></TD>";
                            echo "</TR>";
                            }
                            echo '</TABLE>';
                          }

             echo "<BR><BR><HR SIZE=\"1\"><B><a href=\"$ThisFile\"><B>&lt;&lt; $htext06</a></B><HR SIZE=\"1\"></B>";


?>