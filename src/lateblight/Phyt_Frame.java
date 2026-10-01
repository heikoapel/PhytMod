package lateblight;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.io.*;
import javax.swing.border.*;
import java.lang.String;
import java.lang.Character;
import java.util.jar.*;
import java.util.zip.*;
import java.net.URL;

//import com.borland.jbcl.layout.*;

/**
 * Title:        PhytMod
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      Institute of Geoecology, TU Braunschweig
 * @author Heiko Apel
 * @version 1.0
 */

public class Phyt_Frame extends JFrame {
  JPanel contentPane;
  JMenuBar jMenuBar1 = new JMenuBar();
  JMenu jMenuFile = new JMenu();
  JMenuItem jMenuFileExit = new JMenuItem();
  JMenu jMenuHelp = new JMenu();
  JMenuItem jMenuHelpAbout = new JMenuItem();
  JToolBar jToolBar = new JToolBar();
  JButton jButtonOpen = new JButton();
  JButton jButtonSave = new JButton();
  JButton jButtonHelp = new JButton();
  JButton RunButtonNew = new JButton();
  ImageIcon image1;
  ImageIcon image2;
  ImageIcon image3;
  ImageIcon logo;
  JLabel statusBar = new JLabel();
  JPanel model_def = new JPanel();
//  JScrollPane ModelPane = new JScrollPane(model_def);
  JPanel bottomPanel = new JPanel();
  JTextField jTextField_i = new JTextField();
  JTextField jTextField_l = new JTextField();
  JTextField jTextField_R = new JTextField();
  JTextField jTextField_dt = new JTextField();
  JLabel jLabel_R = new JLabel();
  JLabel jLabel_l = new JLabel();
  JLabel jLabel_i = new JLabel();
  JLabel jLabel_dt = new JLabel();
  BorderLayout borderLayoutContent = new BorderLayout();
  GridBagLayout gridBagLayoutModDef = new GridBagLayout();
  JLabel jLabel1 = new JLabel();
  JRadioButton StandardRadioButton = new JRadioButton();
  JRadioButton PhytRadioButton = new JRadioButton();
  JLabel jLabel_W = new JLabel();
  JLabel jLabel_sp = new JLabel();
  JTextField jTextField_W = new JTextField();
  JTextField jTextField_sp = new JTextField();
  JLabel jLabel_tmax = new JLabel();
  JLabel jLabel_init = new JLabel();
  JTextField jTextField_tmax = new JTextField();
  JTextField jTextField_init = new JTextField();
  JTabbedPane tabbedPane = new JTabbedPane();
  JPanel GraphicsPanel = new JPanel();
  JPanel jPanel_Graph = new JPanel();
  FlowLayout flowLayout2 = new FlowLayout();
  GridBagLayout gridBagLayoutGraphics = new GridBagLayout();
  JPanel jPanel_GraphVis = new JPanel();
  GridBagLayout gridBagLayoutGraphicsVis = new GridBagLayout();
  FlowLayout flowLayout3 = new FlowLayout();
  JPanel GraphicsPanelVis = new JPanel();

  double[][] data;
  PhytKernel Calculation = new PhytKernel();
  String[] columnNames = {"uninfected U", "latent L", "infectious I","dead D", "visible infection (I + D)", "mass balance", "#"};

  boolean dirty = false;
  String expFileName = null;
  String currFileName = null;
  String impFileName = null;
  double[][] dataPoints;
  boolean impDataFormat = false;
  boolean drawPoints = false;

  DefaultTableModel dataModel = new DefaultTableModel(new Object[100][7], columnNames);
  JTable DataPanel = new JTable(dataModel);
  JScrollPane scrollPane = new JScrollPane(DataPanel);
  JMenuItem jMenuItem_Import = new JMenuItem();
  JMenuItem jMenuItem_Export = new JMenuItem();
  JMenu jMenu_Model = new JMenu();
  JMenuItem jMenuItem_RunSame = new JMenuItem();
  JMenuItem jMenuItem_Open = new JMenuItem();
  JMenuItem jMenuItem_Save = new JMenuItem();
  JFileChooser jFileChooserSave = new JFileChooser();
  ExampleFileFilter filter = new ExampleFileFilter(new String("pmf"), "PhytMod files");
  JFileChooser jFileChooserExp = new JFileChooser();
  ExampleFileFilter expFilter = new ExampleFileFilter(new String("dat"), "ASCII file, space delimited");
  JFileChooser jFileChooserImp = new JFileChooser();
  JFileChooser jFileChooserHelp = new JFileChooser();
  ExampleFileFilter htmlFilter  = new ExampleFileFilter(new String("html"), "PhytMod HTML help files");
  JMenuItem jMenuItem_Documentation = new JMenuItem();
  JProgressBar ProgressBar = new JProgressBar();
  Timer timer;
  GridLayout gridLayoutBottomPanel = new GridLayout();
  JMenuItem jMenuItem_RunNew = new JMenuItem();
  JButton RunButtonSame = new JButton();
  JLabel jLabel_PesticideChoice = new JLabel();
  JRadioButton jRadioButton_ChoiceNo = new JRadioButton();
  JRadioButton jRadioButton_ChoicePro = new JRadioButton();
//  JRadioButton jRadioButton3 = new JRadioButton();
  JRadioButton jRadioButton_ChoiceCur = new JRadioButton();
  JRadioButton jRadioButton_ChoiceEra = new JRadioButton();
  TitledBorder titledBorder1;
  JLabel jLabel_AppDays = new JLabel();
  JTextField jTextField_AppDays = new JTextField();
  JRadioButton jRadioButton_Int = new JRadioButton();
  JRadioButton jRadioButton_user = new JRadioButton();
  TitledBorder titledBorder2;
  Border border1;
  JTextField jTextField_Int = new JTextField();
  JLabel jLabel_start = new JLabel();
  JTextField jTextField_start = new JTextField();

  public GraphPane graphicAll = new GraphPane();
  public GraphPane graphicVis = new GraphPane();
  JMenuItem jMenuItem_saveAs = new JMenuItem();
  JMenuItem jMenuItem_Help = new JMenuItem();
//  GraphPane graphicAll = new GraphPane("all", data, Calculation.tmax, Calculation.lat, Calculation.inf, Calculation.dt, columnNames);
//  GraphPane graphicVis = new GraphPane("visible", data, Calculation.tmax, Calculation.lat, Calculation.inf, Calculation.dt, columnNames);

  String baseURLStr;
  JCheckBox jCheckBox_ImportData = new JCheckBox();


  /**Construct the frame*/
  public Phyt_Frame() {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  /**Component initialization*/
  private void jbInit() throws Exception  {
    image1 = new ImageIcon(lateblight.Phyt_Frame.class.getResource("openFile.gif"));
    image2 = new ImageIcon(lateblight.Phyt_Frame.class.getResource("closeFile.gif"));
    image3 = new ImageIcon(lateblight.Phyt_Frame.class.getResource("help.gif"));
    logo = new ImageIcon(lateblight.Phyt_Frame.class.getResource("logo.gif"));
    //setIconImage(Toolkit.getDefaultToolkit().createImage(Phyt_Frame.class.getResource("[Your Icon]")));
    contentPane = (JPanel) this.getContentPane();
    titledBorder1 = new TitledBorder("");
    titledBorder2 = new TitledBorder("");
    border1 = BorderFactory.createBevelBorder(BevelBorder.LOWERED,Color.white,Color.white,new Color(178, 178, 178),new Color(124, 124, 124));
    contentPane.setLayout(borderLayoutContent);
    this.setDefaultCloseOperation(3);
    this.setFont(new java.awt.Font("DialogInput", 1, 12));
    this.setSize(new Dimension(800, 655));
    this.setIconImage(Toolkit.getDefaultToolkit().createImage(Phyt_Frame.class.getResource("logo.gif")));
    this.setTitle("PhytMod - Simulating Phytophthora epidemics");
    this.addWindowListener(new Phyt_Frame_this_windowAdapter(this));
    statusBar.setBorder(BorderFactory.createEtchedBorder());
    statusBar.setMaximumSize(new Dimension(3000, 21));
    statusBar.setPreferredSize(new Dimension(250, 21));
    statusBar.setToolTipText("");
    statusBar.setText(" Ready to go.");
    statusBar.setName("statusBar");
    jMenuFile.setText("File");
    jMenuFileExit.setText("Exit");
    jMenuFileExit.addActionListener(new Phyt_Frame_jMenuFileExit_ActionAdapter(this));
    jMenuHelp.setText("Help");
    jMenuHelpAbout.setText("About");
    jMenuHelpAbout.addActionListener(new Phyt_Frame_jMenuHelpAbout_ActionAdapter(this));
    jButtonOpen.setIcon(new ImageIcon(Phyt_Frame.class.getResource("openFile.gif")));
    jButtonOpen.addActionListener(new Phyt_Frame_jButtonOpen_actionAdapter(this));
    jButtonOpen.setToolTipText("Open File");
    jButtonSave.setIcon(image2);
    jButtonSave.addActionListener(new Phyt_Frame_jButtonSave_actionAdapter(this));
    jButtonSave.setToolTipText("Save model definiition");
    jButtonHelp.setIcon(image3);
    jButtonHelp.addActionListener(new Phyt_Frame_jButtonHelp_actionAdapter(this));
    jButtonHelp.setToolTipText("Help");
    jTextField_i.setMaximumSize(new Dimension(40, 15));
    jTextField_i.setMinimumSize(new Dimension(40, 15));
    jTextField_i.setPreferredSize(new Dimension(40, 15));
    jTextField_i.setToolTipText("specify length of infectious period in days");
    jTextField_i.setText("11");
//    jTextField_i.addActionListener(new Phyt_Frame_jTextField_i_actionAdapter(this));
    jTextField_l.setMaximumSize(new Dimension(40, 15));
    jTextField_l.setMinimumSize(new Dimension(40, 15));
    jTextField_l.setPreferredSize(new Dimension(40, 15));
    jTextField_l.setToolTipText("specify length of latent period in days");
    jTextField_l.setText("3");
    jTextField_R.setMaximumSize(new Dimension(40, 15));
    jTextField_R.setMinimumSize(new Dimension(40, 15));
    jTextField_R.setPreferredSize(new Dimension(40, 15));
    jTextField_R.setToolTipText("specify infection rate");
    jTextField_R.setText("0.33");
    model_def.setBackground(new Color(66, 177, 92));
    model_def.setBorder(BorderFactory.createEtchedBorder());
    model_def.setMaximumSize(new Dimension(192, 800));
    model_def.setMinimumSize(new Dimension(192, 600));
    model_def.setPreferredSize(new Dimension(192, 600));
    model_def.setLayout(gridBagLayoutModDef);
    jLabel_R.setForeground(Color.black);
    jLabel_R.setText("infection rate R");
    jLabel_l.setForeground(Color.black);
    jLabel_l.setText("latent period l");
    jLabel_i.setForeground(Color.black);
    jLabel_i.setText("infect. period i");
    jToolBar.setBorder(BorderFactory.createEtchedBorder());
    jLabel1.setFont(new java.awt.Font("Dialog", 1, 14));
    jLabel1.setForeground(Color.blue);
    jLabel1.setText("Model definition");
    StandardRadioButton.setBackground(new Color(66, 177, 92));
    StandardRadioButton.setBorder(null);
    StandardRadioButton.setMaximumSize(new Dimension(120, 20));
    StandardRadioButton.setMinimumSize(new Dimension(120, 20));
    StandardRadioButton.setPreferredSize(new Dimension(120, 20));
    StandardRadioButton.setMnemonic('0');
    StandardRadioButton.setSelected(true);
    StandardRadioButton.setText("Standard Model");
    StandardRadioButton.addMouseListener(new Phyt_Frame_StandardRadioButton_mouseAdapter(this));
    PhytRadioButton.setBackground(new Color(66, 177, 92));
    PhytRadioButton.setBorder(null);
    PhytRadioButton.setMaximumSize(new Dimension(120, 20));
    PhytRadioButton.setMinimumSize(new Dimension(120, 20));
    PhytRadioButton.setPreferredSize(new Dimension(120, 20));
    PhytRadioButton.setText("Phytophthora Model");
    PhytRadioButton.addMouseListener(new Phyt_Frame_PhytRadioButton_mouseAdapter(this));
    jLabel_W.setForeground(Color.black);
    jLabel_W.setMaximumSize(new Dimension(100, 17));
    jLabel_W.setMinimumSize(new Dimension(100, 17));
    jLabel_W.setPreferredSize(new Dimension(100, 17));
    jLabel_W.setText("lesion growth W");
    jLabel_sp.setForeground(Color.black);
    jLabel_sp.setText("sporulation intensity");
    jTextField_W.setEnabled(false);
    jTextField_W.setMaximumSize(new Dimension(40, 15));
    jTextField_W.setMinimumSize(new Dimension(40, 15));
    jTextField_W.setPreferredSize(new Dimension(40, 15));
    jTextField_W.setToolTipText("for Phytophthora model: specifiy lesion growth rate W (<= 1)");
    jTextField_W.setText("0.3");
    jTextField_sp.setEnabled(false);
    jTextField_sp.setMaximumSize(new Dimension(40, 15));
    jTextField_sp.setMinimumSize(new Dimension(40, 15));
    jTextField_sp.setPreferredSize(new Dimension(40, 15));
    jTextField_sp.setToolTipText("for Phytophthora model: specifiy sporulation intensity");
    jTextField_sp.setText("3");
    jLabel_tmax.setForeground(Color.black);
    jLabel_tmax.setToolTipText("");
    jLabel_tmax.setText("runtime");
    jLabel_init.setForeground(Color.black);
    jLabel_init.setText("initial infection");
    jTextField_tmax.setText("90");
    jTextField_tmax.setToolTipText("specifiy length of model run in days");
    jTextField_tmax.setMaximumSize(new Dimension(40, 15));
    jTextField_tmax.setMinimumSize(new Dimension(40, 15));
    jTextField_tmax.setPreferredSize(new Dimension(40, 15));
    jTextField_init.setText("0.01");
    jTextField_init.setToolTipText("specify initial infection level");
    jTextField_init.setMaximumSize(new Dimension(40, 15));
    jTextField_init.setMinimumSize(new Dimension(40, 15));
    jTextField_init.setPreferredSize(new Dimension(40, 15));
    RunButtonNew.setMaximumSize(new Dimension(60, 27));
    RunButtonNew.setMinimumSize(new Dimension(60, 27));
    RunButtonNew.setPreferredSize(new Dimension(60, 27));
    RunButtonNew.setToolTipText("run model and draw new graph");
    RunButtonNew.setIcon(new ImageIcon(Phyt_Frame.class.getResource("run.gif")));
    RunButtonNew.setMargin(new Insets(0, 0, 0, 0));
    RunButtonNew.addActionListener(new Phyt_Frame_RunButtonNew_actionAdapter(this));
    jLabel_dt.setText("stepsize");
    jLabel_dt.setForeground(Color.black);
    jTextField_dt.setMaximumSize(new Dimension(40, 15));
    jTextField_dt.setMinimumSize(new Dimension(40, 15));
    jTextField_dt.setPreferredSize(new Dimension(40, 15));
    jTextField_dt.setToolTipText("specify calculation stepsize in days (<= 1)");
    jTextField_dt.setText("1");
    jMenuItem_Import.setToolTipText("import data for visible infection");
    jMenuItem_Import.setText("Import");
    jMenuItem_Import.addActionListener(new Phyt_Frame_jMenuItem_Import_actionAdapter(this));
    jMenuItem_Export.setToolTipText("Export model data to ASCII-file");
    jMenuItem_Export.setText("Export");
    jMenuItem_Export.addActionListener(new Phyt_Frame_jMenuItem_Export_actionAdapter(this));
    jMenu_Model.setText("Model");
    jMenuItem_RunSame.setToolTipText("Run Model and draw graphics in the same frame with previous runs");
    jMenuItem_RunSame.setText("Run same");
    jMenuItem_RunSame.addActionListener(new Phyt_Frame_jMenuItem_RunSame_actionAdapter(this));
    jMenuItem_Open.setToolTipText("Open PhytMod File");
    jMenuItem_Open.setText("Open");
    jMenuItem_Open.addActionListener(new Phyt_Frame_jMenuItem_Open_actionAdapter(this));
    jMenuItem_Save.setToolTipText("Save model run to file");
    jMenuItem_Save.setText("Save");
    jMenuItem_Save.addActionListener(new Phyt_Frame_jMenuItem_Save_actionAdapter(this));
    GraphicsPanel.setLayout(gridBagLayoutGraphics);
    jPanel_Graph.setBackground(Color.white);
    jPanel_Graph.setMaximumSize(new Dimension(600, 800));
    jPanel_Graph.setMinimumSize(new Dimension(200, 150));
    jPanel_Graph.setPreferredSize(new Dimension(400, 300));
//    jPanel_Graph.addComponentListener(new Phyt_Frame_jPanel_Graph_componentAdapter(this));
    jPanel_Graph.setLayout(flowLayout2);
    GraphicsPanel.setOpaque(false);
    GraphicsPanel.setPreferredSize(new Dimension(550, 600));
//    GraphicsPanel.addComponentListener(new Phyt_Frame_GraphicsPanel_componentAdapter(this));
    scrollPane.setOpaque(false);
    contentPane.setPreferredSize(new Dimension(800, 800));
//    contentPane.addMouseListener(new Phyt_Frame_contentPane_mouseAdapter(this));
//    contentPane.addComponentListener(new Phyt_Frame_contentPane_componentAdapter(this));
    jMenuItem_Documentation.setText("Documentation");
    jMenuItem_Documentation.addActionListener(new Phyt_Frame_jMenuItem_Documentation_actionAdapter(this));
    bottomPanel.setLayout(gridLayoutBottomPanel);
    bottomPanel.setName("bottomPanel");
    ProgressBar.setBorder(BorderFactory.createEtchedBorder());
    ProgressBar.setMinimumSize(new Dimension(10, 21));
    ProgressBar.setPreferredSize(new Dimension(150, 21));
    jMenuItem_RunNew.setToolTipText("Run model and draw new graph");
    jMenuItem_RunNew.setText("Run new");
    jMenuItem_RunNew.addActionListener(new Phyt_Frame_jMenuItem_RunNew_actionAdapter(this));
    RunButtonSame.addActionListener(new Phyt_Frame_RunButtonSame_actionAdapter(this));
    RunButtonSame.addActionListener(new Phyt_Frame_RunButtonSame_actionAdapter(this));
    RunButtonSame.setMargin(new Insets(0, 0, 0, 0));
    RunButtonSame.setToolTipText("Run Model and draw graphics in the same frame with previous runs");
    RunButtonSame.setIcon(new ImageIcon(Phyt_Frame.class.getResource("runAgain.gif")));
    RunButtonSame.setPreferredSize(new Dimension(60, 27));
    RunButtonSame.setMinimumSize(new Dimension(60, 27));
    RunButtonSame.setMaximumSize(new Dimension(60, 27));
    jLabel_PesticideChoice.setFont(new java.awt.Font("Dialog", 1, 12));
    jLabel_PesticideChoice.setForeground(Color.blue);
    jLabel_PesticideChoice.setToolTipText("");
    jLabel_PesticideChoice.setText("Fungicide application");
    jRadioButton_ChoiceNo.setBackground(new Color(66, 177, 92));
    jRadioButton_ChoiceNo.setEnabled(false);
    jRadioButton_ChoiceNo.setBorder(null);
    jRadioButton_ChoiceNo.setSelected(true);
    jRadioButton_ChoiceNo.setText("no");
    jRadioButton_ChoicePro.setBackground(new Color(66, 177, 92));
    jRadioButton_ChoicePro.setEnabled(false);
    jRadioButton_ChoicePro.setBorder(null);
    jRadioButton_ChoicePro.setText("protective");
    jRadioButton_ChoiceCur.setText("curative");
    jRadioButton_ChoiceCur.setBackground(new Color(66, 177, 92));
    jRadioButton_ChoiceCur.setEnabled(false);
    jRadioButton_ChoiceCur.setBorder(null);
    jLabel_AppDays.setText("application days");
    jLabel_AppDays.setForeground(Color.blue);
    jLabel_AppDays.setFont(new java.awt.Font("Dialog", 1, 12));
    jTextField_AppDays.setEnabled(false);
    jTextField_AppDays.setMaximumSize(new Dimension(100, 15));
    jTextField_AppDays.setMinimumSize(new Dimension(100, 15));
    jTextField_AppDays.setPreferredSize(new Dimension(100, 15));
    jTextField_AppDays.setToolTipText("specify application days.");
    jTextField_AppDays.setText("7,14,21,28,35");
    jRadioButton_Int.setBackground(new Color(66, 177, 92));
    jRadioButton_Int.setEnabled(false);
    jRadioButton_Int.setBorder(null);
    jRadioButton_Int.setSelected(true);
    jRadioButton_Int.setText("interval");
    jRadioButton_Int.addMouseListener(new Phyt_Frame_jRadioButton_Int_mouseAdapter(this));
    jRadioButton_user.setBackground(new Color(66, 177, 92));
    jRadioButton_user.setEnabled(false);
    jRadioButton_user.setBorder(null);
    jRadioButton_user.setText("user");
    jRadioButton_user.addMouseListener(new Phyt_Frame_jRadioButton_user_mouseAdapter(this));
    jTextField_Int.setEnabled(false);
    jTextField_Int.setMaximumSize(new Dimension(32, 15));
    jTextField_Int.setMinimumSize(new Dimension(32, 15));
    jTextField_Int.setPreferredSize(new Dimension(32, 15));
    jTextField_Int.setToolTipText("specify application interval length");
    jTextField_Int.setText("7");
    jLabel_start.setText("start");
    jTextField_start.setEnabled(false);
    jTextField_start.setMaximumSize(new Dimension(20, 15));
    jTextField_start.setMinimumSize(new Dimension(20, 15));
    jTextField_start.setPreferredSize(new Dimension(25, 15));
    jTextField_start.setText("10");
    //ModelPane = scrollable Panel for definition
//    ModelPane.getViewport().setBackground(Color.orange);
//    ModelPane.setPreferredSize(new Dimension(210, 600));
//    contentPane.add(ModelPane, BorderLayout.WEST);
    jPanel_GraphVis.setLayout(flowLayout3);
//    jPanel_GraphVis.addComponentListener(new Phyt_Frame_jPanel_GraphVis_componentAdapter(this));
    jPanel_GraphVis.setPreferredSize(new Dimension(400, 300));
    jPanel_GraphVis.setMinimumSize(new Dimension(200, 150));
    jPanel_GraphVis.setMaximumSize(new Dimension(600, 800));
    jPanel_GraphVis.setBackground(Color.white);
//    GraphicsPanelVis.addComponentListener(new Phyt_Frame_GraphicsPanelVis_componentAdapter(this));
    GraphicsPanelVis.setPreferredSize(new Dimension(550, 600));
    GraphicsPanelVis.setOpaque(false);
    GraphicsPanelVis.setLayout(gridBagLayoutGraphicsVis);
    jMenuItem_saveAs.setText("Save as");
    jMenuItem_saveAs.addActionListener(new Phyt_Frame_jMenuItem_saveAs_actionAdapter(this));
    jMenuItem_Help.setText("Help");
    jMenuItem_Help.addActionListener(new Phyt_Frame_jMenuItem_Help_actionAdapter(this));
    jCheckBox_ImportData.setBackground(new Color(66, 177, 92));
    jCheckBox_ImportData.setText("show imported data");
    jCheckBox_ImportData.setToolTipText("shows imported data, if present");
    jCheckBox_ImportData.setEnabled(false);
    jCheckBox_ImportData.addActionListener(new Phyt_Frame_jCheckBox_ImportData_actionAdapter(this));
    jRadioButton_ChoiceEra.setBorder(null);
    jRadioButton_ChoiceEra.setEnabled(false);
    jRadioButton_ChoiceEra.setBackground(new Color(66, 172, 92));
    jRadioButton_ChoiceEra.setText("eradicant");
    bottomPanel.add(statusBar, null);
    bottomPanel.add(ProgressBar, null);
    jToolBar.add(jButtonOpen);
    jToolBar.add(jButtonSave);
    jToolBar.add(jButtonHelp);
    jToolBar.add(RunButtonSame, null);
    jToolBar.add(RunButtonNew, null);
    contentPane.add(tabbedPane, BorderLayout.CENTER);
    tabbedPane.add(GraphicsPanel,   "all stages");
    GraphicsPanel.add(jPanel_Graph, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(50, 50, 50, 50), 50, 50), 0);
    tabbedPane.add(GraphicsPanelVis,    "visible infection");
    tabbedPane.add(scrollPane,  "model data");
    GraphicsPanelVis.add(jPanel_GraphVis, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(50, 50, 50, 50), 50, 50), 0);
    contentPane.add(model_def, BorderLayout.WEST);
    model_def.add(jTextField_R,       new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 5, 5));
    model_def.add(jTextField_i,       new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 5, 5));
    model_def.add(jLabel_R,          new GridBagConstraints(0, 3, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    model_def.add(jLabel_i,          new GridBagConstraints(0, 2, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    model_def.add(jTextField_l,       new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 5, 5));
    model_def.add(StandardRadioButton,       new GridBagConstraints(0, 7, 4, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 20, 10));
    model_def.add(PhytRadioButton,       new GridBagConstraints(0, 8, 4, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 20, 10));
      // Group the radio buttons.
    ButtonGroup ButtonGroup_model = new ButtonGroup();
    ButtonGroup_model.add(StandardRadioButton);
    ButtonGroup_model.add(PhytRadioButton);
    ButtonGroup ButtonGroup_Pesticide = new ButtonGroup();
    ButtonGroup_Pesticide.add(jRadioButton_ChoiceNo);
    ButtonGroup_Pesticide.add(jRadioButton_ChoicePro);
    ButtonGroup_Pesticide.add(jRadioButton_ChoiceCur);
    ButtonGroup_Pesticide.add(jRadioButton_ChoiceEra);
    ButtonGroup ButtonGroup_Apps = new ButtonGroup();
    ButtonGroup_Apps.add(jRadioButton_Int);
    ButtonGroup_Apps.add(jRadioButton_user);

    model_def.add(jLabel_W,       new GridBagConstraints(0, 9, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    model_def.add(jLabel_sp,       new GridBagConstraints(0, 10, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    model_def.add(jTextField_W,       new GridBagConstraints(3, 9, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 5, 5));
    model_def.add(jTextField_sp,       new GridBagConstraints(3, 10, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 5, 5));
    model_def.add(jLabel_tmax,       new GridBagConstraints(0, 5, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    contentPane.add(jToolBar,  BorderLayout.NORTH);
    jMenuFile.add(jMenuItem_Open);
    jMenuFile.add(jMenuItem_Save);
    jMenuFile.add(jMenuItem_saveAs);
    jMenuFile.addSeparator();
    jMenuFile.add(jMenuItem_Import);
    jMenuFile.add(jMenuItem_Export);
    jMenuFile.addSeparator();
    jMenuFile.add(jMenuFileExit);
    jMenuHelp.add(jMenuItem_Help);
    jMenuHelp.add(jMenuItem_Documentation);
    jMenuHelp.addSeparator();
    jMenuHelp.add(jMenuHelpAbout);
    jMenuBar1.add(jMenuFile);
    jMenuBar1.add(jMenu_Model);
    jMenuBar1.add(jMenuHelp);
    model_def.add(jTextField_tmax,       new GridBagConstraints(3, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 5, 5));
    model_def.add(jTextField_init,       new GridBagConstraints(3, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 5, 5));
    model_def.add(jLabel_init,       new GridBagConstraints(0, 4, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
    model_def.add(jLabel_dt,       new GridBagConstraints(0, 6, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    model_def.add(jTextField_dt,       new GridBagConstraints(3, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 5, 5));
    model_def.add(jLabel_PesticideChoice,       new GridBagConstraints(0, 11, 4, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 0, 5), 0, 0));
    jMenu_Model.add(jMenuItem_RunNew);
    jMenu_Model.add(jMenuItem_RunSame);
    contentPane.add(bottomPanel, BorderLayout.SOUTH);
    model_def.add(jRadioButton_ChoiceNo,       new GridBagConstraints(0, 12, 4, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 5, 2, 2), 0, 0));
    model_def.add(jRadioButton_ChoicePro,       new GridBagConstraints(0, 13, 4, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 5, 2, 2), 0, 0));
    model_def.add(jRadioButton_ChoiceCur,       new GridBagConstraints(0, 14, 4, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 5, 2, 2), 0, 0));
    model_def.add(jLabel_AppDays,         new GridBagConstraints(0, 16, 4, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 5, 0), 0, 0));
    model_def.add(jRadioButton_ChoiceCur, new GridBagConstraints(0, 14, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    model_def.add(jTextField_AppDays,          new GridBagConstraints(2, 18, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 15, 5), 5, 5));
    model_def.add(jRadioButton_Int,        new GridBagConstraints(0, 17, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    model_def.add(jLabel1,         new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 0, 5, 0), 0, 0));
    model_def.add(jRadioButton_user,        new GridBagConstraints(3, 17, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    model_def.add(jTextField_Int,        new GridBagConstraints(2, 17, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 5, 5));
    model_def.add(jLabel_start,            new GridBagConstraints(0, 18, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 15, 5), 0, 0));
    model_def.add(jTextField_start,          new GridBagConstraints(1, 18, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 15, 5), 5, 5));
    model_def.add(jCheckBox_ImportData,                             new GridBagConstraints(0, 19, 4, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
    model_def.add(jRadioButton_ChoiceEra,           new GridBagConstraints(0, 15, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 5, 2, 2), 0, 0));
    model_def.add(jLabel_l,   new GridBagConstraints(0, 1, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
    this.setJMenuBar(jMenuBar1);
    graphicAll.setBackground(Color.white);
    graphicAll.setMaximumSize(new Dimension(600, 800));
    graphicAll.setMinimumSize(new Dimension(200, 150));
    graphicAll.setPreferredSize(new Dimension(400, 300));
    graphicAll.setLayout(flowLayout2);

    jFileChooserExp.addChoosableFileFilter(expFilter);
    jFileChooserExp.setDialogTitle("Export data");
    jFileChooserImp.addChoosableFileFilter(expFilter);
    jFileChooserImp.setDialogTitle("Import data");
    jFileChooserSave.addChoosableFileFilter(filter);
    jFileChooserHelp.addChoosableFileFilter(htmlFilter);
    jFileChooserHelp.setDialogTitle("Locate HTML help files of PhytMod");
  }


  /**File | Exit action performed*/
  public void jMenuFileExit_actionPerformed(ActionEvent e) {
    if (okToAbandon()) {
      System.exit(0);
    }
  }
  /**Help | About action performed*/
  public void jMenuHelpAbout_actionPerformed(ActionEvent e) {
    Phyt_Frame_AboutBox dlg = new Phyt_Frame_AboutBox(this);
    Dimension dlgSize = dlg.getPreferredSize();
    Dimension frmSize = getSize();
    Point loc = getLocation();
    dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
    dlg.setModal(true);
    dlg.show();
  }
  /**Overridden so we can exit when window is closed*/
  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      jMenuFileExit_actionPerformed(null);
    }
  }


  void RunModel() {
    int i, j;
    // try reading variables and model specification
    try { Calculation.latD = Double.parseDouble(jTextField_l.getText().trim());
          Calculation.infD = Double.parseDouble(jTextField_i.getText().trim());
          Calculation.tmax = Integer.parseInt(jTextField_tmax.getText().trim());
          Calculation.R = Double.parseDouble(jTextField_R.getText().trim());
          Calculation.y0 = Double.parseDouble(jTextField_init.getText().trim());
          Calculation.dt = Double.parseDouble(jTextField_dt.getText().trim());
          if (Calculation.dt > 1) {
            jTextField_dt.setText("1");
            Calculation.dt = 1;
          }
          if (!((Calculation.dt == 0.1) || (Calculation.dt == 0.2) || (Calculation.dt == 0.5) || (Calculation.dt == 1))) {
            Calculation.dt = 0.5;
            jTextField_dt.setText("0.5");
            errorMsg("invalid stepsize. stepsize corrected to 0.5 days.\nNote - valid stepsizes: 0.1, 0.2, 0.5, 1");
          }
          Calculation.W = Double.parseDouble(jTextField_W.getText().trim());
          if (Calculation.W > 1) {
            jTextField_W.setText("1");
            Calculation.W = 1;
          }
          Calculation.spint = Double.parseDouble(jTextField_sp.getText().trim());
          Calculation.appInterval = Integer.parseInt(jTextField_Int.getText().trim());
          String apps = jTextField_AppDays.getText().trim();
	  if (jRadioButton_user.isSelected()) {
	    int noApps = 0; int searchStart = 0;
	    while (apps.indexOf(",", searchStart) != -1) {
	      noApps = noApps+1;
	      searchStart = apps.indexOf(",",searchStart)+1;
	    }
	    int[] pos = new int[noApps];
	    searchStart = 0;
            for (i = 0; i < pos.length; i++) {
	      pos[i] = apps.indexOf(",",searchStart);
	      searchStart = apps.indexOf(",",searchStart)+1;
	    }
	    Calculation.appDays = new int[pos.length+1];
            if (pos.length == 0) {
              Calculation.appDays[0] = Integer.parseInt(jTextField_AppDays.getText().trim());
              Calculation.appStart = Integer.parseInt(jTextField_AppDays.getText().trim());
            }
            else {
              for (i = 0; i <= pos.length; i++) {
                if (i == 0) {
                  Calculation.appDays[i] = Integer.parseInt(apps.substring(0, pos[i]));
                  Calculation.appStart = Integer.parseInt(apps.substring(0, pos[i]));
                }
                else if (i > 0 && i < pos.length) {
                  Calculation.appDays[i] = Integer.parseInt(apps.substring(pos[i-1]+1, pos[i]));
                }
                else if (i == pos.length) {
                  Calculation.appDays[i] = Integer.parseInt(apps.substring(pos[i-1]+1));
                }
              }
            }
	  } // end if JRadioButton_user.isSelected()

          if (jRadioButton_Int.isSelected()) {
            Calculation.appStart = Integer.parseInt(jTextField_start.getText().trim());
            Calculation.appDays = new int[(Calculation.tmax-Calculation.appStart)/Calculation.appInterval+1];
            for (i = 0; i < Calculation.appDays.length; i++) {
              Calculation.appDays[i] = Calculation.appStart+i*Calculation.appInterval;
            }
          }// end if JRadioButton_Int.isSelected()

          // if application days > tmax
          for(i = 0; i < Calculation.appDays.length; i++) {
            if (Calculation.appDays[i] > Calculation.tmax) {
              break;
            }
          }
          if (i < Calculation.appDays.length-1) {
            int[] newAppDays = new int[i];
            for(i = 0; i < newAppDays.length; i++) {
              newAppDays[i] = Calculation.appDays[i];
            }
            Calculation.appDays = new int[newAppDays.length];
            for (i = 0; i < newAppDays.length; i++) {
              Calculation.appDays[i] = newAppDays[i];
            }
          }

          // reducing actual application days minus one because of array indexing starting with 0, see kernel
          for(i = 0; i < Calculation.appDays.length; i++) {
            Calculation.appDays[i] = Calculation.appDays[i]-1;
          }

          if (StandardRadioButton.isSelected()) {
            data = Calculation.executeKernel();
//            data = Calculation.InterpolKernel();
          }
          else if (PhytRadioButton.isSelected()) {
            if (jRadioButton_ChoiceNo.isSelected()) {
              data = Calculation.executePhytKernel();
            }
            else if (jRadioButton_ChoicePro.isSelected()) {
              data = Calculation.executePhytKernelPro();
            }
            else if (jRadioButton_ChoiceCur.isSelected()) {
              data = Calculation.executePhytKernelCur();
            }
            else if (jRadioButton_ChoiceEra.isSelected()) {
              data = Calculation.executePhytKernelEra();
            }
          }
      statusBar.setText("Ready.");
    } //end try
    catch (NumberFormatException ex) {
      statusBar.setText("Input Error! Only Numbers are valid.");
      dataModel.setRowCount(1);
      dataModel.setValueAt("Non-numeric input!",0,0);
      dataModel.setValueAt("Please correct.",0,1);
      dataModel.setValueAt("",0,2);
      dataModel.setValueAt("",0,3);
      errorMsg("Input error!\nNon-numeric input or wrong number format in model definition.\nPlease correct.");
    }
  } //end RunModel()

  // fills the Table
  void fillTable() {
    int i, j, start;
    try {
      start = (int) ((Calculation.latD+Calculation.infD)/Calculation.dt);
      int rowNr = (int) ((Calculation.tmax)/Calculation.dt)+1;
      dataModel.setRowCount(rowNr);
      for(i = start; i < (int) ((Calculation.tmax+Calculation.latD+Calculation.infD)/Calculation.dt)+1; i++) {
        for(j = 0; j < Calculation.vars; j++) {
         dataModel.setValueAt(Double.toString(data[j][i]),i-start, j);
        }
        dataModel.setValueAt(Double.toString(data[2][i]+data[3][i]), i-start, 4);
        dataModel.setValueAt(Double.toString(data[0][i]+data[1][i]+data[2][i]+data[3][i]), i-start, 5);
        dataModel.setValueAt(Integer.toString(i-start),i-start, 6);
      }
    }
    catch (ArrayIndexOutOfBoundsException e) {
      errorMsg("array out of bounds. sorry.");
    }
    catch (NoSuchMethodError e) {
      errorMsg(e.toString());
      this.repaint();
    }
  } // end fillTable

  void drawGraphics() {
      graphicAll = new GraphPane("all", data, Calculation.tmax, Calculation.latD, Calculation.infD, Calculation.dt, columnNames, dataPoints, drawPoints);
      GraphicsPanel.remove(0);
      GraphicsPanel.add(graphicAll, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(50, 50, 50, 50), 50, 50),0);
      GraphicsPanel.repaint();
      graphicVis = new GraphPane("visible", data, Calculation.tmax, Calculation.latD, Calculation.infD, Calculation.dt, columnNames, dataPoints, drawPoints);
      GraphicsPanelVis.remove(0);
      GraphicsPanelVis.add(graphicVis, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(50, 50, 50, 50), 50, 50),0);
      GraphicsPanelVis.repaint();
  }

  void estimate() {
    ParamEstimation estim = new ParamEstimation(Calculation);
    estim.runEstimation(estim);
  }

  void openFile(String fileName) {
    try {
      File file = new File(fileName);
      int size = (int)file.length();
      int chars_read = 0;
      FileReader in = new FileReader(file);
      char[] chars = new char[size];
      // Liest alle verfügbaren Zeichen in den Puffer ein.
      while(in.ready()) {
          // Erhöht die Zahl der gelesenen Zeichen
          // und schreibt sie in der Puffer.
        chars_read += in.read(chars, chars_read, size - chars_read);
      }
      in.close();

      String text[] = new String[14];
      int i, j, k;
      j = 0; k = 0;
      for (i = 0; i < chars.length; i++) {
        if (chars[i] == '\n' && k < 14) {
            text[k] = new String(chars, i-(i-j), i-j);
            text[k] = text[k].substring(0,text[k].indexOf("#")).trim();
            k++;
            j = i;
        }
      }
      jTextField_l.setText(text[0]);
      jTextField_i.setText(text[1]);
      jTextField_R.setText(text[2]);
      jTextField_init.setText(text[3]);
      jTextField_tmax.setText(text[4]);
      jTextField_dt.setText(text[5]);
      jTextField_W.setText(text[6]);
      jTextField_sp.setText(text[7]);
      jTextField_Int.setText(text[8]);
      jTextField_start.setText(text[9]);
      jTextField_AppDays.setText(text[10]);
      if (Integer.parseInt(text[11]) == 0) {
        StandardRadioButton.setSelected(true);
        jRadioButton_Int.setEnabled(false);
        jRadioButton_user.setEnabled(false);
        jRadioButton_ChoiceNo.setEnabled(false);
        jRadioButton_ChoicePro.setEnabled(false);
        jRadioButton_ChoiceCur.setEnabled(false);
        jTextField_W.setEnabled(false);
        jTextField_sp.setEnabled(false);
        jTextField_Int.setEnabled(false);
        jTextField_start.setEnabled(false);
        jTextField_AppDays.setEnabled(false);
      }
      else if (Integer.parseInt(text[11]) == 1) {
        PhytRadioButton.setSelected(true);
        jRadioButton_ChoiceNo.setEnabled(true);
        jRadioButton_ChoicePro.setEnabled(true);
        jRadioButton_ChoiceCur.setEnabled(true);
        jTextField_W.setEnabled(true);
        jTextField_sp.setEnabled(true);
        jRadioButton_Int.setEnabled(true);
        jRadioButton_user.setEnabled(true);
        if (jRadioButton_Int.isSelected()) {
          jTextField_Int.setEnabled(true);
          jTextField_start.setEnabled(true);
          jTextField_AppDays.setEnabled(false);
        }
        else if (jRadioButton_user.isSelected()) {
          jTextField_AppDays.setEnabled(true);
          jTextField_Int.setEnabled(false);
          jTextField_start.setEnabled(false);
        }
      }
      if (Integer.parseInt(text[12]) == 0) {
        jRadioButton_ChoiceNo.setSelected(true);
      }
      else if (Integer.parseInt(text[12]) == 1) {
        jRadioButton_ChoicePro.setSelected(true);
      }
      else if (Integer.parseInt(text[12]) == 2) {
        jRadioButton_ChoiceCur.setSelected(true);
      }
      else if (Integer.parseInt(text[12]) == 3) {
        jRadioButton_ChoiceEra.setSelected(true);
      }
      if (Integer.parseInt(text[13]) == 0) {
        jRadioButton_Int.setSelected(true);
      }
      else if (Integer.parseInt(text[13]) == 1) {
        jRadioButton_user.setSelected(true);
      }

      String all = new String(chars);
      if (all.indexOf("dataFile:") != -1) {
        impFileName = all.substring(all.indexOf("dataFile:")+9).trim();
        importFile(impFileName);
      }

      statusBar.setText("opened "+fileName);
      this.currFileName = fileName;
      this.dirty = false;
      updateCaption();
      RunModel();
      drawGraphics();
    }
    catch (IOException e) {
      statusBar.setText("Error opening file "+fileName);
    }
  } // end openFile()

  void importFile(String fileName) {
    try {
      File file = new File(fileName);
      int size = (int)file.length();
      int chars_read = 0;
      FileReader in = new FileReader(file);
      char[] chars = new char[size];
      // Liest alle verfügbaren Zeichen in den Puffer ein.
      while(in.ready()) {
          // Erhöht die Zahl der gelesenen Zeichen
          // und schreibt sie in der Puffer.
        chars_read += in.read(chars, chars_read, size - chars_read);
      }
      in.close();

      int i, j, k;
      int Lines = 0;
      j = 0; k = 0;
      // count lines in import file
      for (i = 0; i < chars.length; i++) {
        if (chars[i] == '\n') {
          Lines++;
        }
      }
      // create Strings for each line and initialize dataPoints array
      String[] rows = new String[Lines];
      dataPoints = new double[Lines][2];
      for (i = 0; i < chars.length; i++) {
        if (chars[i] == '\n') {
          rows[k] = new String(chars, i-(i-j), i-j).trim();
//          errorMsg(rows[k]);
          k++;
          j = i;

        }
      }
      // search Whitespaces and read data into dataPoints array
      char[] row;
      int lastSpace = 0;
      int[] spaces = {0,0};
      for (i = 0; i < Lines; i++) {
        row = rows[i].toCharArray();
        k = 0;
        lastSpace = 0;
        for (j = 0; j < row.length; j++) {
          if (Character.isWhitespace(row[j]) && k < 2) {
            if (j-lastSpace > 1) {
              spaces[k] = j;
//              errorMsg("k = "+Integer.toString(k)+"\n"+"spaces[k] = "+Integer.toString(spaces[k]));
              k++;
            }
            lastSpace = j;
          }
        }
        if (spaces[1] == 0) {
          spaces[1] = row.length;
        }
//        errorMsg("day = "+new String(row, 0, spaces[0]-1).trim());
        dataPoints[i][0] = Double.parseDouble(new String(row, 0, spaces[0]-1).trim());
        dataPoints[i][1] = Double.parseDouble(new String(row, spaces[0], spaces[1]-spaces[0]).trim());
//        errorMsg(Double.toString(dataPoints[i][0])+"\n"+Double.toString(dataPoints[i][1]));
        spaces[0] = 0; spaces[1] = 0;
       }
      // check data format of imported file
      i = 0;
      impDataFormat = true;
      while(i < dataPoints.length) {
        if (dataPoints[i][1] > 1.0) {
          impDataFormat = false;
          errorMsg("Format of imported data is not correct! (visible infection > 1)\nPlease correct in import data file.");
          break;
        }
        i++;
      }

      if (impDataFormat) {
        statusBar.setText("imported data from "+fileName);
        this.impFileName = fileName;
        jCheckBox_ImportData.setSelected(true);
        jCheckBox_ImportData.setEnabled(true);
        drawPoints = true;
      }

      RunModel();
      fillTable();
      drawGraphics();
      this.dirty = true;
      updateCaption();
    }
    catch (IOException e) {
      errorMsg("Error reading file "+fileName+"\nPossibly deleted or moved?");
    }
    catch (IndexOutOfBoundsException e) {
      errorMsg("Error reading data from file "+fileName);
    }
  } // end importFile

  boolean saveFile() {
    if (currFileName == null) {
      return saveAsFile();
    }

    try {
      // Öffnet eine Datei mit dem aktuellen Namen.
      File file = new File (currFileName);

      // Erstellt einen FileWriter, der in diese Datei schreibt.
      // FileWriter konvertiert internationale Zeichencodierungen.
      FileWriter out = new FileWriter(file);
      String outText = "";
      outText = jTextField_l.getText()+"# //latD\n";
      outText = outText + jTextField_i.getText()+"# //infD\n";
      outText = outText + jTextField_R.getText()+"# //R\n";
      outText = outText + jTextField_init.getText()+"# //initial\n";
      outText = outText + jTextField_tmax.getText()+"# //tmax\n";
      outText = outText + jTextField_dt.getText()+"# //dt\n";
      outText = outText + jTextField_W.getText()+"# //W\n";
      outText = outText + jTextField_sp.getText()+"# //sp_int\n";
      outText = outText + jTextField_Int.getText()+"# //interval\n";
      outText = outText + jTextField_start.getText()+"# //start\n";
      outText = outText + jTextField_AppDays.getText()+"# //appDays\n";
      if (StandardRadioButton.isSelected()) {
        outText = outText + "0# //model selection\n";
      }
      else {
        outText = outText + "1# //model selection\n";
      }
      if (jRadioButton_ChoiceNo.isSelected()) {
        outText = outText + "0# //psm selection\n";
      }
      else if (jRadioButton_ChoicePro.isSelected()) {
        outText = outText + "1# //psm selection\n";
      }
      else if (jRadioButton_ChoiceCur.isSelected()) {
        outText = outText + "2# //psm selection\n";
      }
      else if (jRadioButton_ChoiceEra.isSelected()) {
        outText = outText + "3# //psm selection\n";
      }
      if (jRadioButton_Int.isSelected()) {
        outText = outText + "0# //psm mode selection\n";
      }
      else if (jRadioButton_user.isSelected()) {
        outText = outText + "1# //psm mode selection\n";
      }
      if (drawPoints) {
        outText = outText + "dataFile:"+impFileName;
      }
      out.write(outText);
      out.flush();
      out.close();
      this.dirty = false;

      statusBar.setText("Saved to " + currFileName);
      this.dirty = false;
      updateCaption();
      return true;
    }
    catch (IOException e) {
      statusBar.setText("Error saving "+currFileName);
    }
    return false;
  } // end save()

  boolean saveAsFile() {
    if (JFileChooser.APPROVE_OPTION == jFileChooserSave.showSaveDialog(this)) {
      currFileName = jFileChooserSave.getSelectedFile().getPath();
      if (currFileName.lastIndexOf('.') == -1) {
        currFileName = currFileName + ".pmf";
      }
      else if (!currFileName.substring(currFileName.lastIndexOf('.')).equalsIgnoreCase(".pmf")) {
          currFileName = currFileName + ".pmf";
      }
      this.repaint();
      return saveFile();
    }
    else {
      this.repaint();
      return false;
    }
  } // end saveAs()

  boolean okToAbandon() {
  if (!dirty) {
    return true;
  }
     int value =  JOptionPane.showConfirmDialog(this, "Save changes in model definition to file?", "PhytMod message", JOptionPane.YES_NO_CANCEL_OPTION) ;

     switch (value) {
        case JOptionPane.YES_OPTION:
            return saveFile();
        case JOptionPane.NO_OPTION:
            return true;
        case JOptionPane.CANCEL_OPTION:
        default:
            return false;
       }
    }

// updating frame title; Aktualisiert die Titelleiste der Anwendung, so daß der Dateinamen und der Status 'dirty' angezeigt werden.
   void updateCaption() {
     String caption;
     if (currFileName == null) {
        caption = "Simulating Phytophthora epidemics";
     }
     else {
       caption = currFileName;
     }

     if (dirty) {
       caption = caption + " *";
     }
     caption = "PhytMod - " + caption;
     this.setTitle(caption);
   }

  boolean exportFile() {
    String outStr = "";
    String zeile = "";
    if (data != null) {
      if (JFileChooser.APPROVE_OPTION == jFileChooserExp.showSaveDialog(this)) {
        expFileName = jFileChooserExp.getSelectedFile().getPath();
      if (expFileName.lastIndexOf('.') == -1) {
        expFileName = expFileName + ".dat";
      }
      else if (!expFileName.substring(expFileName.lastIndexOf('.')).equalsIgnoreCase(".dat")) {
          expFileName = expFileName + ".dat";
      }
        this.repaint();
        File file = new File(expFileName);
      //  Constructing String for export to ASCII-file
        int i,j;
          for(i = (int) ((Calculation.latD+Calculation.infD)/Calculation.dt); i < (int) ((Calculation.tmax+Calculation.latD+Calculation.infD)/Calculation.dt); i=i+(int) (1/Calculation.dt)) {
            zeile = "";
            for(j = 0; j < Calculation.vars; j++) {
              zeile = zeile + "   " + Double.toString(data[j][i]);
            } // end for inner
            if (i > (int) ((Calculation.latD+Calculation.infD)/Calculation.dt)) {
              outStr = outStr + zeile + "\n";
            }
            else {outStr = zeile + "\n";}
          } // end for outer
          try {
            FileWriter out = new FileWriter(expFileName);
            out.write(outStr);
            out.close();
            statusBar.setText("Data exported to "+expFileName);
//            return true;
          }
          catch (IOException e) {
            statusBar.setText("Error exporting data to "+expFileName);
          }
        } // end if FileChooser.Approve_Option....
      else {
        this.repaint();
        return false;
      }
    return true;
    } // end if != null
    else {
      errorMsg("No data for export available.\nRun model first.");
      return false;
    }
  }


  void RunButtonNew_actionPerformed(ActionEvent e) {
    RunModel();
    fillTable();
    drawGraphics();
    this.dirty = true;
    updateCaption();
//    estimate();
  } //end runbutton action performed

  void jMenuItem_RunNew_actionPerformed(ActionEvent e) {
    RunModel();
    fillTable();
    drawGraphics();
    this.dirty = true;
    updateCaption();
  }

  void RunButtonSame_actionPerformed(ActionEvent e) {
    RunModel();
    if (graphicAll.isShowing()) {
      GraphDraw graph = new GraphDraw(graphicAll);
      graph.getDimension();
      graph.drawAxes(Calculation.tmax);
      graph.drawData(data, Calculation.latD, Calculation.infD, Calculation.dt, columnNames);
    }
    else if (graphicVis.isShowing()) {
      GraphDraw graph = new GraphDraw(graphicVis);
      graph.getDimension();
      graph.drawAxes(Calculation.tmax);
      graph.drawVis(data, Calculation.latD, Calculation.infD, Calculation.dt);
    }
    else if (jPanel_Graph.isShowing()) {
      GraphDraw graph = new GraphDraw(jPanel_Graph);
      graph.getDimension();
      graph.drawAxes(Calculation.tmax);
      graph.drawData(data, Calculation.latD, Calculation.infD, Calculation.dt, columnNames);
    }
    else if (jPanel_GraphVis.isShowing()) {
      GraphDraw graph = new GraphDraw(jPanel_GraphVis);
      graph.getDimension();
      graph.drawAxes(Calculation.tmax);
      graph.drawVis(data, Calculation.latD, Calculation.infD, Calculation.dt);
    }
  }

  void jMenuItem_RunSame_actionPerformed(ActionEvent e) {
    RunModel();
    if (graphicAll.isShowing()) {
      GraphDraw graph = new GraphDraw(graphicAll);
      graph.getDimension();
      graph.drawAxes(Calculation.tmax);
      graph.drawData(data, Calculation.latD, Calculation.infD, Calculation.dt, columnNames);
    }
    else if (graphicVis.isShowing()) {
      GraphDraw graph = new GraphDraw(graphicVis);
      graph.getDimension();
      graph.drawAxes(Calculation.tmax);
      graph.drawVis(data, Calculation.latD, Calculation.infD, Calculation.dt);
    }
    else if (jPanel_Graph.isShowing()) {
      GraphDraw graph = new GraphDraw(jPanel_Graph);
      graph.getDimension();
      graph.drawAxes(Calculation.tmax);
      graph.drawData(data, Calculation.latD, Calculation.infD, Calculation.dt, columnNames);
    }
    else if (jPanel_GraphVis.isShowing()) {
      GraphDraw graph = new GraphDraw(jPanel_GraphVis);
      graph.getDimension();
      graph.drawAxes(Calculation.tmax);
      graph.drawVis(data, Calculation.latD, Calculation.infD, Calculation.dt);
    }
  }


  void jMenuItem_Open_actionPerformed(ActionEvent e) {
    if (!okToAbandon()) {
      return;
    }
    if (JFileChooser.APPROVE_OPTION == jFileChooserSave.showOpenDialog(this)) {
      statusBar.setText("Opened "+jFileChooserSave.getSelectedFile().getPath());
      openFile(jFileChooserSave.getSelectedFile().getPath());
    }
  }

  public void errorMsg(String msg) {
    JOptionPane.showMessageDialog(null, msg, "PhytMod message", 1);
  }


  void jMenuItem_Export_actionPerformed(ActionEvent e) {
    exportFile();
  }

  void jRadioButton_user_mouseClicked(MouseEvent e) {
    jTextField_AppDays.setEnabled(true);
    jTextField_Int.setEnabled(false);
    jTextField_start.setEnabled(false);
  }

  void jRadioButton_Int_mouseClicked(MouseEvent e) {
    jTextField_AppDays.setEnabled(false);
    jTextField_Int.setEnabled(true);
    jTextField_start.setEnabled(true);
  }

  void jMenuItem_saveAs_actionPerformed(ActionEvent e) {
    saveAsFile();
  }

  void jMenuItem_Save_actionPerformed(ActionEvent e) {
    saveFile();
  }

  void jButtonSave_actionPerformed(ActionEvent e) {
    saveFile();
  }

  void jButtonOpen_actionPerformed(ActionEvent e) {
    if (!okToAbandon()) {
      return;
    }
    if (JFileChooser.APPROVE_OPTION == jFileChooserSave.showOpenDialog(this)) {
      statusBar.setText("Opened "+jFileChooserSave.getSelectedFile().getPath());
      openFile(jFileChooserSave.getSelectedFile().getPath());
    }
  }

  void openHelp(String doc) {
    HelpFrame help = new HelpFrame(doc);
    help.setSize(500, 700);
    help.show();

    // old code searching for html help files
//    JEditorPane HelpPane = new JEditorPane();
//    URL helpURL;
//    String s = "";
//    try {
//      if (baseURLStr == null) {
//         s = "file:"
//          + System.getProperty("user.dir")
//          + System.getProperty("file.separator")
//          + "lateblight/docs/"+doc;
//          helpURL = new URL(s);
//          /* ...  use the URL to initialize the editor pane  ... */
//          try {
//            HelpPane.setPage(helpURL);
//            HelpPane.setEditable(false);
//            HelpFrame help = new HelpFrame(HelpPane);
//            help.setSize(450,550);
//            help.show();
//          }
//          catch (IOException e) {
//            JOptionPane.showMessageDialog(null, "Can't find help files!\nPlease select location of PhytMod help files under <path PhytMod>/lateblight/docs.", "PhytMod message", 1);
//                if (JFileChooser.APPROVE_OPTION == jFileChooserHelp.showOpenDialog(this)) {
//                  baseURLStr = jFileChooserHelp.getSelectedFile().getPath();
//                  baseURLStr = baseURLStr.substring(0, baseURLStr.lastIndexOf(System.getProperty("file.separator"))+1);
//                  s = "file:"+baseURLStr+doc;
//                  helpURL = new URL(s);
//                  HelpPane.setPage(helpURL);
//                  HelpPane.setEditable(false);
//                  HelpFrame help = new HelpFrame(HelpPane);
//                  help.setSize(450,550);
//                  help.show();
//                }
//            }
//        }
//        else {
//          s = "file:"+baseURLStr+doc;
//          helpURL = new URL(s);
//          HelpPane.setPage(helpURL);
//          HelpPane.setEditable(false);
//          HelpFrame help = new HelpFrame(HelpPane);
//          help.setSize(450,550);
//          help.show();
//        }
//      }
//      catch (Exception ex) {
//          errorMsg("Couldn't create help URL: " + s);
//      }
  }

  void jButtonHelp_actionPerformed(ActionEvent e) {
    openHelp("help.html");
  }

  void jMenuItem_Help_actionPerformed(ActionEvent e) {
    openHelp("help.html");

    //old code with explicit reading of html files from jar
//    try {
//      JarFile jar = new JarFile("e:/java/phytophthora/lateblight/classes/PhytMod.jar");
//      JarEntry jarE = new JarEntry(jar.getEntry("lateblight/docs/help.html"));
//      if (jarE != null) {
//        BufferedReader bf = new BufferedReader(new InputStreamReader(jar.getInputStream(jarE)));
//        long size = jarE.getSize();
//        int chars_read = 0;
//        char[] chars = new char[(int) size];
//        while(bf.ready()) {
//          chars_read += bf.read(chars, chars_read, (int)size - chars_read);
//        }
//        bf.close();
//        String text = new String(chars);
//        HelpFrame help = new HelpFrame(text);
//        help.setSize(450,550);
//        help.show();
//      }
//    }
//    catch (IOException ex) {
//    }
  }

  void jMenuItem_Documentation_actionPerformed(ActionEvent e) {
    openHelp("documentation.html");
  }

  void StandardRadioButton_mouseClicked(MouseEvent e) {
    jRadioButton_Int.setEnabled(false);
    jRadioButton_user.setEnabled(false);
    jRadioButton_ChoiceNo.setEnabled(false);
    jRadioButton_ChoicePro.setEnabled(false);
    jRadioButton_ChoiceCur.setEnabled(false);
    jRadioButton_ChoiceEra.setEnabled(false);
    jTextField_W.setEnabled(false);
    jTextField_sp.setEnabled(false);
    jTextField_Int.setEnabled(false);
    jTextField_start.setEnabled(false);
    jTextField_AppDays.setEnabled(false);
  }

  void PhytRadioButton_mouseClicked(MouseEvent e) {
    jRadioButton_ChoiceNo.setEnabled(true);
    jRadioButton_ChoicePro.setEnabled(true);
    jRadioButton_ChoiceCur.setEnabled(true);
    jRadioButton_ChoiceEra.setEnabled(true);
    jTextField_W.setEnabled(true);
    jTextField_sp.setEnabled(true);
    jRadioButton_Int.setEnabled(true);
    jRadioButton_user.setEnabled(true);
    if (jRadioButton_Int.isSelected()) {
      jTextField_Int.setEnabled(true);
      jTextField_start.setEnabled(true);
      jTextField_AppDays.setEnabled(false);
    }
    else if (jRadioButton_user.isSelected()) {
      jTextField_AppDays.setEnabled(true);
      jTextField_Int.setEnabled(false);
      jTextField_start.setEnabled(false);
    }
  }

  void jMenuItem_Import_actionPerformed(ActionEvent e) {
    if (JFileChooser.APPROVE_OPTION == jFileChooserImp.showOpenDialog(this)) {
      importFile(jFileChooserImp.getSelectedFile().getPath());
    }
  }

  void jCheckBox_ImportData_actionPerformed(ActionEvent e) {
    if(jCheckBox_ImportData.isSelected() && impDataFormat) {
      drawPoints = true;
      drawGraphics();
    }
    else {
      drawPoints = false;
      drawGraphics();
    }
  }

  void this_windowClosed(WindowEvent e) {

  }

  void this_windowClosing(WindowEvent e) {
//    if (okToAbandon()) {
//      System.exit(0);
//    }
  }


} //end class Phyt_Frame



class Phyt_Frame_jMenuFileExit_ActionAdapter implements ActionListener {
  Phyt_Frame adaptee;

  Phyt_Frame_jMenuFileExit_ActionAdapter(Phyt_Frame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jMenuFileExit_actionPerformed(e);
  }
}

class Phyt_Frame_jMenuHelpAbout_ActionAdapter implements ActionListener {
  Phyt_Frame adaptee;

  Phyt_Frame_jMenuHelpAbout_ActionAdapter(Phyt_Frame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jMenuHelpAbout_actionPerformed(e);
  }
}

//class Phyt_Frame_jTextField_i_actionAdapter implements java.awt.event.ActionListener {
//  Phyt_Frame adaptee;
//
//  Phyt_Frame_jTextField_i_actionAdapter(Phyt_Frame adaptee) {
//    this.adaptee = adaptee;
//  }
//  public void actionPerformed(ActionEvent e) {
//    adaptee.jTextField_i_actionPerformed(e);
//  }
//}


class Phyt_Frame_RunButtonNew_actionAdapter implements java.awt.event.ActionListener {
  Phyt_Frame adaptee;

  Phyt_Frame_RunButtonNew_actionAdapter(Phyt_Frame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.RunButtonNew_actionPerformed(e);
  }
}

class Phyt_Frame_jMenuItem_RunSame_actionAdapter implements java.awt.event.ActionListener {
  Phyt_Frame adaptee;

  Phyt_Frame_jMenuItem_RunSame_actionAdapter(Phyt_Frame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jMenuItem_RunSame_actionPerformed(e);
  }
}

class Phyt_Frame_jMenuItem_Open_actionAdapter implements java.awt.event.ActionListener {
  Phyt_Frame adaptee;

  Phyt_Frame_jMenuItem_Open_actionAdapter(Phyt_Frame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jMenuItem_Open_actionPerformed(e);
  }
}

class Phyt_Frame_contentPane_componentAdapter extends java.awt.event.ComponentAdapter {
  Phyt_Frame adaptee;

  Phyt_Frame_contentPane_componentAdapter(Phyt_Frame adaptee) {
    this.adaptee = adaptee;
  }
}

//class Phyt_Frame_contentPane_mouseAdapter extends java.awt.event.MouseAdapter {
//  Phyt_Frame adaptee;
//
//  Phyt_Frame_contentPane_mouseAdapter(Phyt_Frame adaptee) {
//    this.adaptee = adaptee;
//  }
//  public void mouseReleased(MouseEvent e) {
//    adaptee.contentPane_mouseReleased(e);
//  }
//}

class Phyt_Frame_jMenuItem_Export_actionAdapter implements java.awt.event.ActionListener {
  Phyt_Frame adaptee;

  Phyt_Frame_jMenuItem_Export_actionAdapter(Phyt_Frame adaptee){
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jMenuItem_Export_actionPerformed(e);
  }
}

class Phyt_Frame_jMenuItem_RunNew_actionAdapter implements java.awt.event.ActionListener {
  Phyt_Frame adaptee;

  Phyt_Frame_jMenuItem_RunNew_actionAdapter(Phyt_Frame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jMenuItem_RunNew_actionPerformed(e);
  }
}

class Phyt_Frame_RunButtonSame_actionAdapter implements java.awt.event.ActionListener {
  Phyt_Frame adaptee;

  Phyt_Frame_RunButtonSame_actionAdapter(Phyt_Frame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.RunButtonSame_actionPerformed(e);
  }
}

class Phyt_Frame_jRadioButton_user_mouseAdapter extends java.awt.event.MouseAdapter {
  Phyt_Frame adaptee;

  Phyt_Frame_jRadioButton_user_mouseAdapter(Phyt_Frame adaptee) {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e) {
    adaptee.jRadioButton_user_mouseClicked(e);
  }
}

class Phyt_Frame_jRadioButton_Int_mouseAdapter extends java.awt.event.MouseAdapter {
  Phyt_Frame adaptee;

  Phyt_Frame_jRadioButton_Int_mouseAdapter(Phyt_Frame adaptee) {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e) {
    adaptee.jRadioButton_Int_mouseClicked(e);
  }
}

class Phyt_Frame_jMenuItem_saveAs_actionAdapter implements java.awt.event.ActionListener {
  Phyt_Frame adaptee;

  Phyt_Frame_jMenuItem_saveAs_actionAdapter(Phyt_Frame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jMenuItem_saveAs_actionPerformed(e);
  }
}

class Phyt_Frame_jMenuItem_Save_actionAdapter implements java.awt.event.ActionListener {
  Phyt_Frame adaptee;

  Phyt_Frame_jMenuItem_Save_actionAdapter(Phyt_Frame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jMenuItem_Save_actionPerformed(e);
  }
}

class Phyt_Frame_jButtonSave_actionAdapter implements java.awt.event.ActionListener {
  Phyt_Frame adaptee;

  Phyt_Frame_jButtonSave_actionAdapter(Phyt_Frame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jButtonSave_actionPerformed(e);
  }
}

class Phyt_Frame_jButtonOpen_actionAdapter implements java.awt.event.ActionListener {
  Phyt_Frame adaptee;

  Phyt_Frame_jButtonOpen_actionAdapter(Phyt_Frame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jButtonOpen_actionPerformed(e);
  }
}

class Phyt_Frame_jButtonHelp_actionAdapter implements java.awt.event.ActionListener {
  Phyt_Frame adaptee;

  Phyt_Frame_jButtonHelp_actionAdapter(Phyt_Frame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jButtonHelp_actionPerformed(e);
  }
}

class Phyt_Frame_jMenuItem_Help_actionAdapter implements java.awt.event.ActionListener {
  Phyt_Frame adaptee;

  Phyt_Frame_jMenuItem_Help_actionAdapter(Phyt_Frame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jMenuItem_Help_actionPerformed(e);
  }
}

class Phyt_Frame_jMenuItem_Documentation_actionAdapter implements java.awt.event.ActionListener {
  Phyt_Frame adaptee;

  Phyt_Frame_jMenuItem_Documentation_actionAdapter(Phyt_Frame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jMenuItem_Documentation_actionPerformed(e);
  }
}

class Phyt_Frame_StandardRadioButton_mouseAdapter extends java.awt.event.MouseAdapter {
  Phyt_Frame adaptee;

  Phyt_Frame_StandardRadioButton_mouseAdapter(Phyt_Frame adaptee) {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e) {
    adaptee.StandardRadioButton_mouseClicked(e);
  }
}

class Phyt_Frame_PhytRadioButton_mouseAdapter extends java.awt.event.MouseAdapter {
  Phyt_Frame adaptee;

  Phyt_Frame_PhytRadioButton_mouseAdapter(Phyt_Frame adaptee) {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e) {
    adaptee.PhytRadioButton_mouseClicked(e);
  }
}

class Phyt_Frame_jMenuItem_Import_actionAdapter implements java.awt.event.ActionListener {
  Phyt_Frame adaptee;

  Phyt_Frame_jMenuItem_Import_actionAdapter(Phyt_Frame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jMenuItem_Import_actionPerformed(e);
  }
}

class Phyt_Frame_jCheckBox_ImportData_actionAdapter implements java.awt.event.ActionListener {
  Phyt_Frame adaptee;

  Phyt_Frame_jCheckBox_ImportData_actionAdapter(Phyt_Frame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jCheckBox_ImportData_actionPerformed(e);
  }
}

class Phyt_Frame_this_windowAdapter extends java.awt.event.WindowAdapter {
  Phyt_Frame adaptee;

  Phyt_Frame_this_windowAdapter(Phyt_Frame adaptee) {
    this.adaptee = adaptee;
  }
  public void windowClosed(WindowEvent e) {
    adaptee.this_windowClosed(e);
  }
  public void windowClosing(WindowEvent e) {
    adaptee.this_windowClosing(e);
  }
}
