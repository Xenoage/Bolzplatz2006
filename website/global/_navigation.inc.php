<?

    # ====================================================== #
    # BP2k6 - Homepage => Navigation fuer D/E/F              #
    # ------------------------------------------------------ #
    # (c)2006 maus / m.schmitz / maussites.de                #
    # LastChange: 04.08.2006                                 #
    # ====================================================== #


# status / todo:  o.k.

# ______________________________________________________________________________

$NlLanguage = "de";
if ($DebugMode == 1){ echo "[language: $ThisLanguage]"; }
if (($ThisLanguage < 1)or($ThisLanguage >3))  # (ID-DAU..? :)
     {$ThisLanguage = "1";}
$LanguageID = $ThisLanguage -1;
if ($LanguageID == 1) {$NlLanguage = "en";}
# ______________________________________________________________________________


/* Erzeugen von Navigationspunkten:   (Array > MyNav)
   ------------------------------------------------------------------------------
   Syntax: SEITENNAME.php | Anzeigen(D,E,F) | ButtonBezeichnung(D,E,F) | Fenster

           - Seitenname: Die Seite, die mit diesem Punkt verknuepft ist,
                         exakte Schreibweise beachten, z.B. >Presse.php<
           - Aktive Sprache: 1=anzeigen / 0=ausblenden  - fuer D,E,F
           - ButtonBeschriftung: fuer D,E,F
           - _blank = neues Fenster / _self = gleiches Fenster

   -> Absatz zwischen Navigationspunkten: BLANK
   ------------------------------------------------------------------------------ */


$MyNav = array (
   "index.php|1,1,1|Home / News,Home / News,Accueil / Actualit&eacute;|_self",
   "BLANK",
   "information.php|1,1,1|Informationen,Information,Informations|_self",
   "about.php|1,1,1| - Über Bolzplatz2006, - About Slam Soccer 2006, - Sur Coup de Foot 2006|_self",
   "screenshots.php|1,1,1| - Screenshots, - Screenshots, - Copies d'&eacute;cran|_self",
   "development.php|1,1,1| - Entwicklung, - Development, - D&eacute;veloppement|_self",
   "team.php|1,1,1| - Team, - Team, - Le team|_self",
   "BLANK",
   "downloads.php|1,1,1|Downloads,Downloads,T&eacute;l&eacute;chargements|_self",
   "goldedition.php|1,1,1|Gold Edition,Gold Edition,Gold Edition|_self",
   "mods.php|1,1,1|Mods,Mods,Mods|_self",
   "shirts.php|1,1,1|T-Shirts,T-Shirts,T-Shirts|_self",
   "spend.php|1,1,1|Spenden,Voluntary donations,Vos dons|_self",
   "BLANK",
   "halloffame.php|1,1,1|Hall of Fame,Hall of Fame,Hall of Fame|_self",
   "wm.php|1,1,1| - Weltmeisterschaft, - World Cup, - Coupe du Monde|_self",
   "allstarsleague.php|1,1,1| - Allstars League, - Allstars League, - Allstars League|_self",
   "BLANK",
   //"forum.php|1,1,1|Forum,Forums,Forum|_self",
   "faq.php|1,1,1|FAQ,FAQ,FAQ|_self",
   //"../$NlLanguage-newsletter/newsletter.php|1,1,0|Newsletter,Newsletter,Lettre d'information|_self",
   /* "BLANK", //ANDI: no blank here, becase press and links are mostly hidden */
   "press.php|0,0,0|Presse/Reviews,Presse/Reviews,Presse/Critiques|_self",
   "links.php|1,0,0|Links,Links,Liens|_self",
   "BLANK",
   "contact.php|1,1,1|Kontakt,Contact,Contacts|_self",
   "imprint.php|1,1,1|Impressum,Imprint,Mentions l&eacute;gales|_self",
   "../datenschutz.html|1,1,1|Datenschutz,Privacy Policy,Données personnelles|_self",
   "BLANK"
);


# ------------------------------------------------------------------------------

# Config NavColors
$NavColNorm = "#444444";   # NavColor normal
$NavColOn   = "#558800";   # NavColor aktiv
$NavColHigh = "#99aa00";   # NavColor Rollover


# Ausgabe Navigation:  ---------------------------------------------------------

echo '<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="1" WIDTH="161">';

if ($PreDir == "de/") {$NavColNorm = "#f0f0f0";}
foreach ($MyNav as $Menu) {
   $MyMenu = explode("|", $Menu);
   if ($MyMenu[0] == "BLANK")                            # Ausgabe ist ein Absatz Absatz (BLANK)
       {
       echo '<TR><TD COLSPAN="2">&nbsp; </TD></TR>';
       }
       else {                                            # Ausgabe ist ein link:
            $NavNorm = $NavColNorm;
            $NavHigh = $NavColHigh;
            $MyMenuLang = explode(",", $MyMenu[1]);
            if ($MyMenuLang[$LanguageID] == "1")         # ..ist der MenuPunkt aktiv?
                {
                $MyMenuText = explode(",", $MyMenu[2]);  # ..dann hole Bezeichnung:
                $NavBez = $MyMenuText[$LanguageID];      # ...o.k. :)
                if ($MyMenu[3] == "_blank")              # in welchem Fenster/Browser oeffnen?
                    {
                    $OnClick = "onclick=\"window.open('".$MyMenu[0]."')\"";         # link im neuen Fenster
                    }
                    else {
                         $OnClick = "onclick=\"location.href='".$PreDir.$MyMenu[0]."'\"";   # link im selben Fenster
                         }
                $Datei = basename($_SERVER["PHP_SELF"]); # Aktive Seite => markieren..?
                if ($Datei == $MyMenu[0])
                    {
                    $NavNorm = $NavColOn;
                    }

                ?>
                 <TR>
                 <TD WIDTH="5" CLASS="navstart"><IMG SRC="../global/grafik/x.gif" WIDTH="5" HEIGHT="16"></TD>
                 <td bgcolor="<? echo $NavNorm; ?>" onMouseover="this.bgColor='<? echo $NavHigh; ?>'" onMouseout="this.bgColor='<? echo $NavNorm; ?>'" <? echo $OnClick; ?> style="cursor:hand;cursor:pointer" valign="Middle">&nbsp;&nbsp;&nbsp;<span class="nav" style="cursor:hand;"><B><? echo ''.$NavBez.'</a>'; ?></B></span></TD>
                 </TR>
                 <TR><TD COLSPAN="2" HEIGHT="1"><IMG SRC="../global/grafik/x.gif" WIDTH="1" HEIGHT="1"></TD></TR>
                <?
                }
            }
}
echo '<TR><TD COLSPAN="2"><IMG SRC="../global/grafik/x.gif" WIDTH="1" HEIGHT="40"></TD></TR>';
echo '</TABLE> ';


# ------------------------------------------------------------------------------

# MiniStats ;-)
$AvblLanguages = array ("DE","EN","FR");
$MyStatSite = $AvblLanguages[$LanguageID]." -> ".basename($_SERVER["PHP_SELF"]);
?>
<script type="text/javascript" src="../bpstats2/pixel.php?mode=js"></script>


