<?

    if ($DebugMode == "1") {echo "<P><B>DB-On</B><P>";}
    $db=mysql_connect($dbhost,$dbuser,$dbpass);                                     # open db
    mysql_select_db($dbname);

    $Gethof_teams = mysql_query("SELECT * FROM $TableTeams WHERE TeamID='$TeamID'");
    $AktGethof_teams = mysql_fetch_row($Gethof_teams);

    $Gethof_user = mysql_query("SELECT * FROM $TableUsers WHERE FName='$FName'");
    $AktGethof_user = mysql_fetch_row($Gethof_user);

    if ($AktGethof_user[0])    # User schon vorhanden?
        {                                  ## User ist vorhanden:
        if ($DebugMode == "1") {echo "<p>User schon vorhanden";}
        $OldTeam = $AktGethof_user[1];
        $Oldhof_teams = mysql_query("SELECT * FROM $TableTeams WHERE TeamID='$OldTeam'");
        $OldGethof_teams = mysql_fetch_row($Oldhof_teams);
        $OldCount = $OldGethof_teams[3];
        $OldCount --;
        mysql_query("UPDATE $TableUsers SET TeamID='$TeamID', Name='$myname', Datum='$timenow', Land='$myland', Ort='$mytown', SubTstamp='$code', Server='$Server'  WHERE FName='$FName'");
        $Oldbestuser = mysql_query("SELECT * FROM $TableUsers WHERE TeamID='$OldTeam' ORDER BY SubTstamp desc");
        $OldUser = mysql_fetch_row($Oldbestuser);
        $OldBestUser = $OldUser[2];
        $OldBestDate = $OldUser[3];
        mysql_query("UPDATE $TableTeams SET No='$OldCount', LastDate='$OldBestDate', LastName='$OldBestUser' WHERE TeamID='$OldTeam' LIMIT 1");

        if ($AktGethof_teams[0])     # Team schon vorhanden?
            {                                     ## Team ist vorhanden:
            if ($DebugMode == "1") {echo "<br>Team schon vorhanden";}#
            $Gethof_teams = mysql_query("SELECT * FROM $TableTeams WHERE TeamID='$TeamID'");
            $AktGethof_teams = mysql_fetch_row($Gethof_teams);
            $Eintraege = $AktGethof_teams[3];
            $Eintraege ++;
            mysql_query("UPDATE $TableTeams SET No='$Eintraege', LastDate='$timenow', LastName='$myname' WHERE TeamID='$TeamID' LIMIT 1");
            }
            else {                                ## Team neu anlegen:
                 if ($DebugMode == "1") {echo "<br>Team neu angelegt";}
                 mysql_query("INSERT INTO $TableTeams (TeamID, Mode, TeamName, No, Offline, LastDate, LastName )
                              VALUES ('$TeamID', '$gamemode', '$myteamname', '1', '$AutomHoF', '$timenow', '$myname' )"
                              );
                 }
        }
    else {                                 ## User neu anlegen:
         if ($DebugMode == "1") {echo "<p>User neu angelegt";}
         mysql_query("INSERT INTO $TableUsers (TeamID, Name, Datum, Land, Ort, SubTstamp, FName, FPass, Server )
                              VALUES ('$TeamID', '$myname', '$timenow', '$myland', '$mytown', '$code', '$FName', '$FPass', '$Server' )"
                              );
         if ($AktGethof_teams[0])     # Team schon vorhanden?
            {                                     ## Team ist vorhanden:
            $Eintraege = $AktGethof_teams[3];
            $Eintraege ++;
            mysql_query("UPDATE $TableTeams SET No='$Eintraege', LastDate='$timenow', LastName='$myname' WHERE TeamID='$TeamID' LIMIT 1");
            }
            else {                                ## Team neu anlegen:
                 mysql_query("INSERT INTO $TableTeams (TeamID, Mode, TeamName, No, Offline, LastDate, LastName )
                                     VALUES ('$TeamID', '$gamemode', '$myteamname', '1', '$AutomHoF', '$timenow', '$myname' )"
                                     );
                 }
         }

?>