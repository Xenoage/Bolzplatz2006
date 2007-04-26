#include <windows.h>


void GetProgramDir(char* szProgramPath)
{
  GetModuleFileName(NULL, (LPSTR)szProgramPath, MAX_PATH);
  strcpy(strrchr((const char *)szProgramPath, '\\')+1, "");
  return;
}


int main(int argc, char *argv[])
{
  SHELLEXECUTEINFO SE;
	memset(&SE,0,sizeof(SE));
	SE.fMask = SEE_MASK_NOCLOSEPROCESS ;
  char szProgramPath[MAX_PATH + 1];
  GetProgramDir(szProgramPath);
  char szProgramDir[MAX_PATH + 1];
  GetProgramDir(szProgramDir);
	SE.lpFile = strcat(szProgramPath, "lib\\jre\\bin\\javaw");
  SE.lpDirectory = szProgramDir;
	SE.lpParameters = "-classpath \"game.jar;lib/jirr/irrlicht.jar;lib/lwjgl/lwjgl_util.jar;lib/lwjgl/lwjgl.jar;lib/dom4j/dom4j-1.6.1.jar;lib/sdl/sdljava.jar;lib/vecmath.jar;\" -Djava.library.path=\"lib/dom4j;lib/jirr;lib/lwjgl;lib/other;lib/sdl\" com.xenoage.bp2k6.tools.settings.SettingsTool >log2.txt 2>&1";
	SE.nShow = SW_SHOW;
	SE.cbSize = sizeof(SE);
	ShellExecuteEx(&SE);
}
