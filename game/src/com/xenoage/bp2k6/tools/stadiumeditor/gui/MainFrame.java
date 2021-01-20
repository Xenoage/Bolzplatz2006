/**
 * Bolzplatz 2006
 * Copyright (C) 2006 by Xenoage Software
 * 
 * Attention: This file is part of the Gold-Edition
 * of Bolzplatz 2006 and is not released under the GPL!
 * Ask info@xenoage.com if you want to modify it.
 */
package com.xenoage.bp2k6.tools.stadiumeditor.gui;

import com.xenoage.bp2k6.tools.stadiumeditor.StadiumEditor;
import com.xenoage.bp2k6.util.*;
import com.xenoage.bp2k6.util.language.Language;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import net.sf.jirr.*;


/**
 * This is the main frame of the
 * stadium editor (developer mode).
 * 
 * In Linux it contains only the main menu
 * and the toolbar, in windows it contains
 * also the palette bar and the workspace
 * (single document interface).
 * 
 * @author Andreas Wenger
 */
public class MainFrame
  extends JFrame
  implements ActionListener, WindowListener
{
  //the editor main class
  StadiumEditor editor = null;
  
  //this JFrame: allow "this" for inner classes
  JFrame thisFrame;
  
  //menu components
  JMenuBar mainMenuBar;
  JMenu menuStadium;
  JMenuItem menuStadiumNew, menuStadiumOpen, menuStadiumSaveAs, menuStadiumExit;
  JMenu menuEdit;
  JMenuItem menuEditMoveNorth, menuEditMoveSouth,
    menuEditMoveWest, menuEditMoveEast, menuEditMoveUp, menuEditMoveDown,
    menuEditRotateLeft, menuEditRotateRight,
    menuEditModification, menuEditDelete;
  JMenu menuHelp;
  JMenuItem menuHelpInfo;
  
  //toolbar components
  JToolBar mainToolBar;
  JButton tbbNew, tbbOpen, tbbSaveAs, 
    tbb3DViewDefault, tbb3DViewLeft, tbb3DViewRight, tbb3DViewUp, tbb3DViewDown,
    tbb3DViewZoomIn, tbb3DViewZoomOut;
  JToggleButton tbbShowGrid, tbbAlignGrid, tbbTerrain;
  
  //left bar/window
  PalettePanel panelPalette = null;
  PaletteWindow windowPalette = null;
  
  //work space (3d view)
  JPanel panelWorkSpace;
  IrrlichtInterface irrlichtInterface;
  int mainFrameWidth;
  
  
  boolean irrlichtRunning = false;
  //Thread irrlichtThread;
  
  //gaps between components
  int gapSmall = 5;
  int gapLarge = 10;
  
  
  /**
   * Constructor.
   */
  public MainFrame(StadiumEditor editor)
  {
    Logging.log(Logging.LEVEL_MESSAGES, this, "Loading stadium editor...");
    
    this.editor = editor;
    thisFrame = this;
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    //set system look & feel
    //setSystemLookAndFeel();
    //title of stadium editor
    this.setTitle(Language.get("stadiumeditor_title") +
      " - " + Language.get("version") + " " + editor.version);
    //general settings
    this.setIconImage(new ImageIcon("data/images/icons/stadiumeditor.png").getImage());
    if (editor.isOSWindows())
    {
      //Windows: maximized window
      this.setSize(1024, 768);
      this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
    else
    {
      //Linux: default size
    }
    this.addWindowListener(this);
    this.setLayout(new BorderLayout());
    //create menu bar
    createMenuBar();
    //create tool bar
    createToolBar();
    //in Windows: add palette bar and workspace
    if (editor.isOSWindows())
    {
      //create left bar
      panelPalette = new PalettePanel(this, editor, this);
      this.add(panelPalette, BorderLayout.WEST);
      //create workspace
      createWorkspace();
    }
    //in Linux: open palette window and workspace window
    else
    {
      //palette window
      windowPalette = new PaletteWindow(this, editor);
      panelPalette = windowPalette.getPalettePanel();
      windowPalette.pack();
      //pack the main frame
      this.pack();
      //move windows to the right positions
      windowPalette.setLocation(
        Toolkit.getDefaultToolkit().getScreenSize().width / 2 -
          windowPalette.getWidth() / 2 - windowPalette.getHeight() * 4 / 3 / 2,
        Toolkit.getDefaultToolkit().getScreenSize().height / 2 -
        windowPalette.getHeight() / 2 - this.getHeight() / 2);
      windowPalette.setVisible(true);
      this.setLocation(
        windowPalette.getLocation().x,
        windowPalette.getLocation().y - this.getHeight());
      /* this.setPreferredSize(new Dimension(windowPalette.getWidth() +
        windowPalette.getWidth() - windowPalette.getContentPane().getWidth() +
          windowPalette.getContentPane().getHeight() * 4 / 3,
        (int) this.getContentPane().getHeight())); */
      //disable windows
      this.setEnabled(false);
      windowPalette.setEnabled(false);
      //create irrlicht window
      mainFrameWidth = this.getWidth();
      irrlichtInterface = new IrrlichtWindow(this);
      Thread irrlichtThread = new Thread()
      {
        @Override public void run()
        {
          Logging.log(Logging.LEVEL_MESSAGES, null,
            "Thread for Irrlicht started.");
          irrlichtInterface.createIrrlichtDevice(
            windowPalette.getContentPane().getHeight() * 4 / 3,
            windowPalette.getContentPane().getHeight());
          Logging.log(Logging.LEVEL_MESSAGES, null,
            "Thread for Irrlicht ended.");
        }
      };
      irrlichtThread.start();
      
    }
    Logging.log(Logging.LEVEL_MESSAGES, this, "Stadium editor loaded.");
  }
  
  
  /**
   * Action listener for menu and toolbar events.
   */
  public void actionPerformed(ActionEvent e)
  {
    Object obj = e.getSource();
    //menu
    if (obj == menuStadiumNew)
    {
      newStadium();
    }
    else if (obj == menuStadiumOpen)
    {
      openStadium();
    }
    else if (obj == menuStadiumSaveAs)
    {
      showDialogStadiumSaveAs();
    }
    else if (obj == menuStadiumExit)
    {
      if (showSaveQuestion())
        editor.close();
    }
    else if (obj == menuEditMoveNorth)
    {
      editor.keyPressed(KeyEvent.VK_UP);
    }
    else if (obj == menuEditMoveSouth)
    {
      editor.keyPressed(KeyEvent.VK_DOWN);
    }
    else if (obj == menuEditMoveWest)
    {
      editor.keyPressed(KeyEvent.VK_LEFT);
    }
    else if (obj == menuEditMoveEast)
    {
      editor.keyPressed(KeyEvent.VK_RIGHT);
    }
    else if (obj == menuEditMoveUp)
    {
      editor.keyPressed(KeyEvent.VK_W);
    }
    else if (obj == menuEditMoveDown)
    {
      editor.keyPressed(KeyEvent.VK_Y);
    }
    else if (obj == menuEditRotateLeft)
    {
      editor.keyPressed(KeyEvent.VK_A);
    }
    else if (obj == menuEditRotateRight)
    {
      editor.keyPressed(KeyEvent.VK_S);
    }
    else if (obj == menuEditModification)
    {
      editor.keyPressed(KeyEvent.VK_M);
    }
    else if (obj == menuEditDelete)
    {
      editor.keyPressed(KeyEvent.VK_DELETE);
    }
    else if (obj == menuHelpInfo)
    {
      JOptionPane.showMessageDialog(this,
        Language.get("stadiumeditor_title") + "\n\n" +
        "(c) 2006 by Andreas Wenger, Xenoage Software",
        Language.get("stadiumeditor_title"),
        JOptionPane.INFORMATION_MESSAGE);
    }
    //toolbar
    else if (obj == tbbNew)
    {
      newStadium();
    }
    else if (obj == tbbOpen)
    {
      openStadium();
    }
    else if (obj == tbbSaveAs)
    {
      showDialogStadiumSaveAs();
    }
    else if (obj == tbbShowGrid)
    {
      irrlichtInterface.setStadiumGridVisible(tbbShowGrid.isSelected());
    }
    else if (obj == tbbAlignGrid)
    {
      editor.setGridAlign(tbbAlignGrid.isSelected());
    }
    else if (obj == tbbTerrain)
    {
      editor.setTerrainVisible(tbbTerrain.isSelected());
    }
    else if (obj == tbb3DViewDefault)
    {
      irrlichtInterface.getCamera().setDefaultPosition();
    }
    else if (obj == tbb3DViewLeft)
    {
      irrlichtInterface.getCamera().left();
    }
    else if (obj == tbb3DViewRight)
    {
      irrlichtInterface.getCamera().right();
    }
    else if (obj == tbb3DViewUp)
    {
      irrlichtInterface.getCamera().up();
    }
    else if (obj == tbb3DViewDown)
    {
      irrlichtInterface.getCamera().down();
    }
    else if (obj == tbb3DViewZoomIn)
    {
      irrlichtInterface.getCamera().zoomIn();
    }
    else if (obj == tbb3DViewZoomOut)
    {
      irrlichtInterface.getCamera().zoomOut();
    }
    if (irrlichtInterface instanceof Panel)
      ((Panel) irrlichtInterface).repaint();
  }
  
    
  /**
   * Creates the main menu bar.
   */
  private void createMenuBar()
  {
    //create menu bar
    mainMenuBar = new JMenuBar();
    //menu "stadium"
    menuStadium = new JMenu(Language.get("menu_stadium"));
    {
      menuStadiumNew = createMenuItem(Language.get("menu_new"), menuStadium);
      menuStadiumOpen = createMenuItem(Language.get("menu_open"), menuStadium);
      menuStadiumSaveAs = createMenuItem(Language.get("menu_saveas"), menuStadium);
      menuStadium.addSeparator();
      menuStadiumExit = createMenuItem(Language.get("menu_exit"), menuStadium);
    }
    mainMenuBar.add(menuStadium);
    //menu "edit"
    menuEdit = new JMenu(Language.get("menu_edit"));
    {
      menuEditMoveNorth = createMenuItem(Language.get("menu_north"), menuEdit);
      menuEditMoveNorth.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0));
      menuEditMoveSouth = createMenuItem(Language.get("menu_south"), menuEdit);
      menuEditMoveSouth.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0));
      menuEditMoveWest = createMenuItem(Language.get("menu_west"), menuEdit);
      menuEditMoveWest.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0));
      menuEditMoveEast = createMenuItem(Language.get("menu_east"), menuEdit);
      menuEditMoveEast.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0));
      menuEdit.addSeparator();
      menuEditMoveUp = createMenuItem(Language.get("menu_up"), menuEdit);
      menuEditMoveUp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0));
      menuEditMoveDown = createMenuItem(Language.get("menu_down"), menuEdit);
      menuEditMoveDown.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, 0));
      menuEdit.addSeparator();
      menuEditRotateLeft = createMenuItem(Language.get("menu_rotateleft"), menuEdit);
      menuEditRotateLeft.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0));
      menuEditRotateRight = createMenuItem(Language.get("menu_rotateright"), menuEdit);
      menuEditRotateRight.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0));
      menuEdit.addSeparator();
      menuEditModification = createMenuItem(Language.get("menu_modification"), menuEdit);
      menuEditModification.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, 0));
      menuEdit.addSeparator();
      menuEditDelete = createMenuItem(Language.get("menu_delete"), menuEdit);
      menuEditDelete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
    }
    mainMenuBar.add(menuEdit);
    //menu "help"
    menuHelp = new JMenu(Language.get("menu_help"));
    {
      menuHelpInfo = createMenuItem(Language.get("menu_about"), menuHelp);
    }
    mainMenuBar.add(menuHelp);
    //add menu bar to main frame
    this.setJMenuBar(mainMenuBar);
  }
  
  
  /**
   * Returns a JMenuItem with the given caption on the given
   * parent menu and adds an ActionListener to this.
   */
  private JMenuItem createMenuItem(String caption, JMenu parent)
  {
    JMenuItem ret = new JMenuItem(caption);
    ret.addActionListener(this);
    parent.add(ret);
    return ret;
  }
  
  
  /**
   * Creates the main tool bar.
   */
  private void createToolBar()
  {
    //create tool bar
    mainToolBar = new JToolBar();
    mainToolBar.setFloatable(false);
    //button "new"
    tbbNew = createToolBarButton("new", "Neues Stadion", mainToolBar);
    tbbOpen = createToolBarButton("open", "Stadion �ffnen...", mainToolBar);
    tbbSaveAs = createToolBarButton("saveas", "Stadion speichern unter...", mainToolBar);
    mainToolBar.addSeparator();
    tbbShowGrid = createToolBarToggleButton("showgrid", "Raster anzeigen", mainToolBar, true);
    tbbAlignGrid = createToolBarToggleButton("aligngrid", "Am Raster ausrichten", mainToolBar, true);
    tbbTerrain = createToolBarToggleButton("showterrain", "Terrain anzeigen", mainToolBar, true);
    mainToolBar.addSeparator();
    tbb3DViewDefault = createToolBarButton("defaultview", "3D-Ansicht zur�cksetzen", mainToolBar);
    tbb3DViewLeft = createToolBarButton("left", "3D-Ansicht nach links drehen", mainToolBar);
    tbb3DViewRight = createToolBarButton("right", "3D-Ansicht nach rechts drehen", mainToolBar);
    tbb3DViewUp = createToolBarButton("up", "3D-Ansicht nach oben drehen", mainToolBar);
    tbb3DViewDown = createToolBarButton("down", "3D-Ansicht nach unten drehen", mainToolBar);
    tbb3DViewZoomIn = createToolBarButton("zoomin", "3D-Ansicht hineinzoomen", mainToolBar);
    tbb3DViewZoomOut = createToolBarButton("zoomout", "3D-Ansicht herauszoomen", mainToolBar);
    //add tool bar to main frame
    this.add(mainToolBar, BorderLayout.NORTH);
  }
  
  
  /**
   * Returns a JButton with the given icon id
   * (filename in "data/images/buttons/" without ending ".png")
   * and with the given tooltip text on the given
   * parent toolbar and adds an ActionListener to this.
   */
  private JButton createToolBarButton(String iconID, String tooltip, JToolBar parent)
  {
    Icon icon = null;
    //System.out.println("data/images/buttons/" + iconID + ".png");
    if (iconID != null && iconID.length() > 0)
      icon = new ImageIcon("data/images/buttons/" + iconID + ".png");
    JButton ret = new JButton(icon);
    ret.setFocusable(false);
    ret.setToolTipText(tooltip);
    ret.addActionListener(this);
    parent.add(ret);
    return ret;
  }
  
  
  /**
   * Returns a JToggleButton with the given icon id
   * (filename in "data/images/buttons/" without ending ".png")
   * and with the given tooltip text on the given
   * parent toolbar with the given selected value
   * and adds an ActionListener to this.
   */
  private JToggleButton createToolBarToggleButton(String iconID, String tooltip,
    JToolBar parent, boolean selected)
  {
    Icon icon = null;
    if (iconID != null && iconID.length() > 0)
      icon = new ImageIcon("data/images/buttons/" + iconID + ".png");
    JToggleButton ret = new JToggleButton(icon);
    ret.setFocusable(false);
    ret.setToolTipText(tooltip);
    ret.setSelected(selected);
    ret.addActionListener(this);
    parent.add(ret);
    return ret;
  }
  
  
    
  
  /**
   * Creates the work space controls (3d view)
   */
  private void createWorkspace()
  {
    panelWorkSpace = new JPanel();
    panelWorkSpace.setLayout(null);
    irrlichtInterface = new IrrlichtPanel(this);
    Panel panelIrrlicht = (Panel) irrlichtInterface;
    panelIrrlicht.setSize(760, 570);
    panelIrrlicht.setBackground(new Color(200, 200, 200));
    //create irrlicht device when window is completely loaded
    this.addWindowListener(new WindowAdapter()
    {
      @Override public void windowOpened(WindowEvent e)
      {
        if (!irrlichtRunning)
        {
          irrlichtRunning = true;
          irrlichtInterface.createIrrlichtDevice(0, 0);
          //mainframe is now completely created.
          editor.mainFrameLoaded();
        }
      }
    });
    panelWorkSpace.add(panelIrrlicht);
    //center 3d window
    panelWorkSpace.addComponentListener(new ComponentAdapter()
    {
      @Override public void componentResized(ComponentEvent e)
      {
        Panel panelIrrlicht = (Panel) irrlichtInterface;
        panelIrrlicht.setLocation(
          (panelWorkSpace.getWidth() - panelIrrlicht.getWidth()) / 2,
          (panelWorkSpace.getHeight() - panelIrrlicht.getHeight()) / 2);
      }
    });
    this.add(panelWorkSpace, BorderLayout.CENTER);
  }
  
  
  /**
   * Create new stadium.
   */
  private void newStadium()
  {
    if (showSaveQuestion())
      editor.createNewStadium();
  }
  
  
  /**
   * Opens a stadium.
   */
  private void openStadium()
  {
    if (showSaveQuestion())
      showDialogStadiumOpen();
  }
  
  
  /**
   * Show save question, but only if stadium has
   * some content.
   * @return Returns <code>false</code>, if the user
   * klicked the "cancel" button.
   */
  private boolean showSaveQuestion()
  {
    if (editor.isStadiumEmpty())
      return true;
    int i = JOptionPane.showConfirmDialog(this,
      Language.get("stadiumeditor_savequestion_text"),
      Language.get("stadiumeditor_savequestion_title"),
      JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
    if (i == JOptionPane.YES_OPTION)
      showDialogStadiumSaveAs();
    return (i != JOptionPane.CANCEL_OPTION);
  }
  
  
  /**
   * Show the "open stadium" dialog.
   */
  private void showDialogStadiumOpen()
  {
    JFileChooser dlgOpen = new JFileChooser();
    dlgOpen.setDialogTitle(Language.get("stadiumeditor_openstadium_title"));
    dlgOpen.setCurrentDirectory(new java.io.File("data/stadiums/"));
    dlgOpen.addChoosableFileFilter(FileUtils.getStadiumFilter());
    int ret = dlgOpen.showOpenDialog(this);
    if (ret == JFileChooser.APPROVE_OPTION)
    {
      String id = FileUtils.getNameWithoutExtension(dlgOpen.getSelectedFile());
      if (new java.io.File("data/stadiums/" + id + ".xml").exists())
      {
        editor.loadStadium(id);
      }
      else
      {
        showMessageBox(
          "Invalid file or directory.\nUse only the given stadiums directory!", true);
      }
    }
  }
  
  
  /**
   * Show the "save stadium" dialog.
   */
  private void showDialogStadiumSaveAs()
  {
    JFileChooser dlgSave = new JFileChooser();
    dlgSave.setDialogTitle(Language.get("stadiumeditor_savestadium_title"));
    dlgSave.setCurrentDirectory(new java.io.File("data/stadiums/"));
    dlgSave.addChoosableFileFilter(FileUtils.getStadiumFilter());
    int ret = dlgSave.showSaveDialog(this);
    if (ret == JFileChooser.APPROVE_OPTION)
    {
      String id = FileUtils.getNameWithoutExtension(dlgSave.getSelectedFile());
      //if (new java.io.File("data/stadiums/" + id + ".xml").exists())
      //{
        editor.saveStadium(id);
      //}
    }
  }
  
  
  /**
   * Call this method to fill all controls and
   * the workspace with the information of the
   * current stadium object.
   */
  public void updateStadiumData()
  {
    panelPalette.updateStadiumData();
  }
  
  
  /**
   * Gets the Irrlicht scene manager.
   */
  public ISceneManager getSceneManager()
  {
    return irrlichtInterface.getSceneManager();
  }
  
  
  /**
   * Gets the Irrlicht video driver.
   */
  public IVideoDriver getVideoDriver()
  {
    return irrlichtInterface.getVideoDriver();
  }
  
  
  /**
   * Repaints the work space window (3d view)
   */
  public void repaint3DView()
  {
    if (irrlichtInterface instanceof Panel)
      ((Panel) irrlichtInterface).repaint();
  }
  
  
  /**
   * Sets the focus on the work space window (3d view)
   */
  public void focus3DView()
  {
    if (irrlichtInterface instanceof Panel)
      ((Panel) irrlichtInterface).requestFocus();
  }


  
  public StadiumEditor getEditor()
  {
    return editor;
  }


  public void windowOpened(WindowEvent e)
  {
  }


  public void windowClosing(WindowEvent e)
  {
    if (showSaveQuestion())
      editor.close();
  }


  public void windowClosed(WindowEvent e)
  {
  }


  public void windowIconified(WindowEvent e)
  {
  }


  public void windowDeiconified(WindowEvent e)
  {
  }


  public void windowActivated(WindowEvent e)
  {
  }


  public void windowDeactivated(WindowEvent e)
  {
  }
  
  
  /**
   * Gets the name of the stadium.
   */
  public String getStadiumName()
  {
    return panelPalette.getStadiumName();
  }
  
  
  private void showMessageBox(String text, boolean warning)
  {
    JOptionPane.showMessageDialog(this, text,
      Language.get("stadiumeditor_title"),
      (warning ? JOptionPane.WARNING_MESSAGE : JOptionPane.INFORMATION_MESSAGE));
  }
  
  
  /**
   * Call this method when the stadium will be modified.
   * This ensures that the Irrlicht window is not repainted
   * until endEdit() is called, which may cause a crash.
   */
  synchronized public void startEdit()
  {
    if (irrlichtInterface instanceof IrrlichtWindow)
      ((IrrlichtWindow) irrlichtInterface).startEdit();
  }
  
  
  /**
   * Call this method when the modification of the
   * stadium is done. Then the Irrlicht window
   * may be repainted again.
   */
  synchronized public void endEdit()
  {
    if (irrlichtInterface instanceof IrrlichtWindow)
      ((IrrlichtWindow) irrlichtInterface).endEdit();
  }
  
  
  /**
   * Called in Linux when Irrlicht is loaded.
   */
  public void irrlichtLoaded()
  {
    this.setEnabled(true);
    windowPalette.setEnabled(true);
    editor.mainFrameLoaded();
  }
  
  
}
