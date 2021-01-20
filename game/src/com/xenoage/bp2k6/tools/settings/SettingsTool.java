/**
 * Bolzplatz 2006
 * Copyright (C) 2006 by Xenoage Software
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 */
package com.xenoage.bp2k6.tools.settings;

import com.xenoage.bp2k6.Main;
import com.xenoage.bp2k6.util.*;
import com.xenoage.bp2k6.util.language.Language;
import com.xenoage.bp2k6.util.language.LanguageInfo;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;


/**
 * This tool shows a Swing window, in which
 * the user can change some important settings
 * like video mode and audio engine.
 *
 * @author Andreas Wenger
 */
public class SettingsTool
	extends JFrame
	implements ActionListener
{
  
  private Resolution screenRes;
  private ConfigFile audioConfig;
  private ConfigFile langConfig;
  private ArrayList<LanguageInfo> languages;
  
	private JPanel panelMain;
  
	private JComboBox cmbScreenRes;
	private JRadioButton[] optFullscreen = new JRadioButton[2];
  private JCheckBox chkLowQuality;
		
	private JComboBox cmbAudioEngine;
  
  private JComboBox cmbLanguage;
	
	private JButton btnOK;
	private JButton btnCancel;
	private JButton btnDefault;
	
	
	public SettingsTool()
	{
	}
	
	
	public void createFrame()
	{
    
    //title and icon
    setTitle(Language.get("settings"));
    setIconImage(new ImageIcon("data/images/icons/settings.png").getImage());
    
		//main panel
		panelMain = new JPanel();
		panelMain.setLayout(new BoxLayout(panelMain, BoxLayout.PAGE_AXIS));
		panelMain.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		this.getContentPane().add(panelMain);

		//create the video panel
		createVideoPanel();
		
		//create the audio panel
		createAudioPanel();
    
		//create the language panel
    createLanguagePanel();
		
		//create the buttons panel
		createButtonsPanel();
    
    //set size
    pack();
    setResizable(false);
    setLocationRelativeTo(null);
	}
	
	
	private void createVideoPanel()
	{
		//screen panel
		JPanel panelVideo = new JPanel();
		panelVideo.setBorder(BorderFactory.createCompoundBorder(
	    BorderFactory.createTitledBorder(Language.get("video")),
	    BorderFactory.createEmptyBorder(5,5,5,5)));
	  panelVideo.setLayout(new GridLayout(0,1));
	  panelMain.add(panelVideo);
    
	  //load irrlicht library
    Main.loadIrrlichtLibrary();
    //load current settings
    screenRes = new Resolution();
    int currentRes[] = new int[]{0, 0, 0};
    boolean currentFullscreen = false;
    boolean lowQuality = false;
    try
    {
      int videoModeIndex = screenRes.getSelectedVideoMode();
      currentRes = screenRes.getVideoModeByIndex(videoModeIndex);
      currentFullscreen = screenRes.getSelectedFullscreenMode();
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, this, "Current video mode is invalid! Details:");
      Logging.log(Logging.LEVEL_ERRORS, this, ex);
      JOptionPane.showMessageDialog(this,
        Language.get("currentvideomodedoesnotexist"),
        Language.get("error"), JOptionPane.ERROR_MESSAGE);
    }
    //load list of screen resolutions
    int res[][] = screenRes.getAvailableVideoModes();
    String resList[];
    int selectedIndex = 0;
    if (res != null && res.length > 0)
    {
      resList = new String[res.length];
      for (int i = 0; i < res.length; i++)
      {
        resList[i] = res[i][0] + " x " + res[i][1] + " x " + res[i][2];
        if (res[i][0] == currentRes[0] && res[i][1] == currentRes[1] &&
          res[i][2] == currentRes[2])
        {
          selectedIndex = i;
        }
      }
    }
    else
    {
      JOptionPane.showMessageDialog(this,
        Language.get("novideomodelistfound"),
        Language.get("error"), JOptionPane.ERROR_MESSAGE);
      resList = new String[1];
      resList[0] = "640 x 480 x 16";
    }
		cmbScreenRes = new JComboBox(resList);
    cmbScreenRes.setSelectedIndex(selectedIndex);
		cmbScreenRes.addActionListener(this);
		panelVideo.add(cmbScreenRes);
    
		optFullscreen[0] = new JRadioButton(Language.get("windowedmode"));
    optFullscreen[0].setSelected(!currentFullscreen);
	  panelVideo.add(optFullscreen[0]);
	  
	  optFullscreen[1] = new JRadioButton(Language.get("fullscreenmode"));
    optFullscreen[1].setSelected(currentFullscreen);
	  panelVideo.add(optFullscreen[1]);
	  
    lowQuality = ConfigFile.getValueFromFileBoolean(
      "data/config/videoquality.xml", "lowquality");
    
    chkLowQuality = new JCheckBox(Language.get("lowquality"));
    chkLowQuality.setSelected(lowQuality);
    panelVideo.add(chkLowQuality);
    
	  ButtonGroup group = new ButtonGroup();
    group.add(optFullscreen[0]);
    group.add(optFullscreen[1]);

	}
	
	
	private void createAudioPanel()
	{
		//audio panel
		JPanel panelAudio = new JPanel();
		panelAudio.setBorder(BorderFactory.createCompoundBorder(
	    BorderFactory.createTitledBorder(Language.get("audio")),
	    BorderFactory.createEmptyBorder(5,5,5,5)));
	  panelAudio.setLayout(new GridLayout(0,1));
	  panelMain.add(panelAudio);
    
	  //load current settings
    int selectedIndex = 1;
    try
    {
      audioConfig = new ConfigFile("data/config/audio.xml");
      if (audioConfig.getValue("device").equals("javasounddevice"))
        selectedIndex = 0;
      else if (audioConfig.getValue("device").equals("lwjgldevice"))
        selectedIndex = 1;
      else if (audioConfig.getValue("device").equals("nulldevice"))
        selectedIndex = 2;
      else
        JOptionPane.showMessageDialog(this,
          Language.get("unknownaudiodevice"),
          Language.get("error"), JOptionPane.ERROR_MESSAGE);
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, this, "Audio config is invalid! Details:");
      Logging.log(Logging.LEVEL_ERRORS, this, ex);
      JOptionPane.showMessageDialog(this,
        Language.get("audioconfiginvalid"),
        Language.get("error"), JOptionPane.ERROR_MESSAGE);
    }
    
    String[] audioEngines = new String[]{
      Language.get("javasounddevice"),
      Language.get("lwjgldevice"),
      Language.get("nulldevice")};
	  
	  //audio engines
	  cmbAudioEngine = new JComboBox(audioEngines);
    cmbAudioEngine.setSelectedIndex(selectedIndex);
		cmbAudioEngine.addActionListener(this);
		panelAudio.add(cmbAudioEngine);
	}
  
  
  private void createLanguagePanel()
  {
    //language panel
    JPanel panelLanguage = new JPanel();
    panelLanguage.setBorder(BorderFactory.createCompoundBorder(
      BorderFactory.createTitledBorder(Language.get("language")),
      BorderFactory.createEmptyBorder(5,5,5,5)));
    panelLanguage.setLayout(new GridLayout(0,1));
    panelMain.add(panelLanguage);
    
    //get available languages
    languages = Language.getAvailableLanguages();
    
    //load current settings
    int selectedIndex = 0;
    try
    {
      langConfig = new ConfigFile("data/config/language.xml");
      String selectedLang = langConfig.getValue("id");
      for (int i = 0; i < languages.size(); i++)
      {
        if (languages.get(i).getID().equals(selectedLang))
        {
          selectedIndex = i;
          break;
        }
      }
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, this, "Language config is invalid! Details:");
      Logging.log(Logging.LEVEL_ERRORS, this, ex);
      JOptionPane.showMessageDialog(this,
        Language.get("languageconfiginvalid"),
        Language.get("error"), JOptionPane.ERROR_MESSAGE);
    }
    
    String[] langArray = new String[languages.size()];
    for (int i = 0; i < languages.size(); i++)
    {
      langArray[i] = languages.get(i).getLocalName() + " (" +
        languages.get(i).getInternationalName() + ")";
    }
    
    //languages
    cmbLanguage = new JComboBox(langArray);
    cmbLanguage.setSelectedIndex(selectedIndex);
    cmbLanguage.addActionListener(this);
    panelLanguage.add(cmbLanguage);
  }
	
	
	private void createButtonsPanel()
	{
		//buttons panel
		JPanel panelButtons = new JPanel();
		panelButtons.setBorder(BorderFactory.createEmptyBorder(5,0,0,0));
	  panelButtons.setLayout(new GridLayout(0,2));
	  panelMain.add(panelButtons);

	  //buttons
	  btnOK = new JButton(Language.get("ok"));
		btnOK.addActionListener(this);
		panelButtons.add(btnOK);
		
		btnCancel = new JButton(Language.get("cancel"));
		btnCancel.addActionListener(this);
		panelButtons.add(btnCancel);
		
		btnDefault = new JButton("Standard");
		btnDefault.addActionListener(this);
		//do not use this button
    //panelButtons.add(btnDefault);
	}
	
	
	public static void main(String args[])
	{
    Logging.log(Logging.LEVEL_MESSAGES, null, "Starting settings tool...");
    Language.loadLanguage(Language.getSelectedLanguageID());
    SettingsTool app = new SettingsTool();
    app.createFrame();
    app.setVisible(true);
	}
	
	
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == btnOK)
		{
			//save video settings
      try
      {
        if (cmbScreenRes.getItemCount() == 1)
          screenRes.saveSelectedVideoMode(-1, optFullscreen[1].isSelected());
        else
          screenRes.saveSelectedVideoMode(cmbScreenRes.getSelectedIndex(),
            optFullscreen[1].isSelected());
      }
      catch (Exception ex)
      {
        Logging.log(Logging.LEVEL_ERRORS, this, "Error while saving video config:");
        Logging.log(Logging.LEVEL_ERRORS, this, ex);
        JOptionPane.showMessageDialog(this,
          Language.get("savevideoconfigerror"),
          Language.get("error"), JOptionPane.ERROR_MESSAGE);
      }
      //save video quality
      try
      {
        ConfigFile videoQuality = new ConfigFile("data/config/videoquality.xml");
        videoQuality.setValue("lowquality",
          String.valueOf(chkLowQuality.isSelected()));
        videoQuality.saveToXML();
      }
      catch (Exception ex)
      {
        Logging.log(Logging.LEVEL_ERRORS, this,
          "Error while saving video quality config:");
        Logging.log(Logging.LEVEL_ERRORS, this, ex);
        JOptionPane.showMessageDialog(this,
          Language.get("savevideoqualityconfigerror"),
          Language.get("error"), JOptionPane.ERROR_MESSAGE);
      }
      //save audio settings
      try
      {
        if (cmbAudioEngine.getSelectedIndex() == 0)
          audioConfig.setValue("device", "javasounddevice");
        else if (cmbAudioEngine.getSelectedIndex() == 1)
          audioConfig.setValue("device", "lwjgldevice");
        else
          audioConfig.setValue("device", "nulldevice");
        audioConfig.saveToXML();
      }
      catch (Exception ex)
      {
        Logging.log(Logging.LEVEL_ERRORS, this, "Error while saving audio config:");
        Logging.log(Logging.LEVEL_ERRORS, this, ex);
        JOptionPane.showMessageDialog(this,
          Language.get("saveaudioconfigerror"),
          Language.get("error"), JOptionPane.ERROR_MESSAGE);
      }
      //save language settings
      try
      {
        langConfig.setValue("id", languages.get(
          cmbLanguage.getSelectedIndex()).getID());
        langConfig.saveToXML();
      }
      catch (Exception ex)
      {
        Logging.log(Logging.LEVEL_ERRORS, this, "Error while saving language config:");
        Logging.log(Logging.LEVEL_ERRORS, this, ex);
        JOptionPane.showMessageDialog(this,
          Language.get("savelanguageconfigerror"),
          Language.get("error"), JOptionPane.ERROR_MESSAGE);
      }
      System.exit(0);
		}
		else if (e.getSource() == btnCancel)
		{
			//cancel
			if (JOptionPane.showConfirmDialog(null,
				Language.get("exitwithoutsaving"), Language.get("settings"),
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) ==
          JOptionPane.YES_OPTION)
			{
        System.exit(0);
			}
		}
    else if (e.getSource() == btnDefault)
    {
      //restore default settings
      JOptionPane.showMessageDialog(this,
        Language.get("notyetimplemented"),
        "", JOptionPane.INFORMATION_MESSAGE);
    }
  }


  @Override protected void processWindowEvent(WindowEvent e) 
  {
    if (e.getID() == e.WINDOW_CLOSING) 
    {
      //cancel
      if (JOptionPane.showConfirmDialog(null,
        Language.get("exitwithoutsaving"), Language.get("settings"),
        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) ==
          JOptionPane.YES_OPTION)
      {
        System.exit(0);
      }
    }
  }
	
}