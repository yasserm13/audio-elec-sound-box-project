/*
    Classe avec toutes les options de la Sound Box
 */

import javax.sound.midi.*;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

public class SoundBox {

    private JPanel jPanel;
    private ArrayList<JCheckBox> checkBoxList;
    private Sequencer sequencer;
    private Sequence sequence;
    private Track track;
    private JFrame jFrame;
    private String[] instrumentsNames = {"Bass Drum", "Closed Hi-Hat",
            "Open Hi-Hat","Acoustic Snare", "Crash Cymbal", "Hand Clap",
            "High Tom", "Hi Bongo", "Maracas", "Whistle", "Low Conga",
            "Cowbell", "Vibraslap", "Low-mid Tom", "High Agogo",
            "Open Hi Conga", "Customized sound"};
    private int[] instruments = {35,42,46,38,49,39,50,60,70,72,64,56,58,47,67,63,63};
    private float nombreBpm = 120;
    JSlider jSliderBpm;
    JButton jButtonStart = new JButton("Start");
    JComboBox jComboBox;
    Vector data = new Vector(instrumentsNames.length);

    SoundBox() {

        jFrame = new JFrame("AudioElecBeatBox");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        BorderLayout layout = new BorderLayout();
        JPanel background = new JPanel(layout);
        background.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JMenuBar menuBar = new JMenuBar();

        JMenu menuFile = (JMenu) menuBar.add(new JMenu("File"));
        JMenuItem itemExit = (JMenuItem) menuFile.add(new JMenuItem("Exit"));
        itemExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { System.exit(0); }
        });

        JMenu menuOption = (JMenu) menuBar.add(new JMenu("Options"));
        JMenuItem itemClear = (JMenuItem) menuOption.add(new JMenuItem("Clear Boxes"));
        itemClear.addActionListener(new ButtonClearBoxesListener());

        background.add(menuBar, BorderLayout.NORTH);

        checkBoxList = new ArrayList<JCheckBox>();
        Box controlBox = new Box(BoxLayout.Y_AXIS);

       // JButton jButtonStart = new JButton("Start");
        jButtonStart.addActionListener(new ButtonStartListener());
        controlBox.add(jButtonStart);

        JButton jButtonStop = new JButton("Stop");
        jButtonStop.addActionListener(new ButtonStopListener());
        controlBox.add(jButtonStop);

        JButton jButtonClearBoxes = new JButton("Clear Boxes");
        jButtonClearBoxes.addActionListener(new ButtonClearBoxesListener());
        controlBox.add(jButtonClearBoxes);

        //DÃ©claration JSlider
        jSliderBpm = new JSlider(40,160,(int) nombreBpm);
        //Set le titre
        TitledBorder tb = new TitledBorder(new EtchedBorder());
        tb.setTitle("Tempo BPM =" + jSliderBpm.getValue());
        jSliderBpm.setBorder(tb);
        //Action qui sera executÃ© lors du dÃ©placement du slider
        jSliderBpm.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                nombreBpm = jSliderBpm.getValue();

                TitledBorder tb = new TitledBorder(new EtchedBorder());
                tb.setTitle("Tempo BPM =" + jSliderBpm.getValue());
                jSliderBpm.setBorder(tb);
            }
        });
        //Ajout du slider en dessous
        controlBox.add(jSliderBpm);

        JButton jButtonTempoUp = new JButton("Tempo Up (+5 bpm)");
        jButtonTempoUp.addActionListener(new ButtonTempoUpListener());
        controlBox.add(jButtonTempoUp);

        JButton jButtonTempoDown = new JButton("Tempo Down (-5 bpm)");
        jButtonTempoDown.addActionListener(new ButtonTempoDownListener());
        controlBox.add(jButtonTempoDown);
        
        //Ajout du choix des beats pré-enregistré ()
        jComboBox = new JComboBox();
        jComboBox.addItem("Rock Beat 1");
        jComboBox.addItem("Rock Beat 2");
        jComboBox.addItem("Rock Beat 3");
        jComboBox.addActionListener(new ButtonPresetTracks());
        controlBox.add(jComboBox);

        Box box = new Box(BoxLayout.Y_AXIS);
        for (int i = 0; i < 17; i++) {
            box.add(new Label(instrumentsNames[i]));
        }

        background.add(BorderLayout.EAST, controlBox);
        background.add(BorderLayout.WEST, box);

        jFrame.getContentPane().add(background);

        GridLayout grid = new GridLayout(17,16);
        grid.setVgap(1);
        grid.setHgap(1);
        jPanel = new JPanel(grid);
        background.add(BorderLayout.CENTER, jPanel);

        for (int i = 0; i < 272; i++) {
            JCheckBox c = new JCheckBox();
            c.setSelected(false);
            checkBoxList.add(c);
            jPanel.add(c);
        }

        setUpMidi();

        jFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        jFrame.pack();
        jFrame.setVisible(true);
    }

    public void setUpMidi() {
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequence = new Sequence(Sequence.PPQ,4);
            track = sequence.createTrack();

            sequencer.setTempoInBPM(nombreBpm);

        } catch(Exception e) {e.printStackTrace();}
    }

    public class ButtonStartListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {
            sequencer.stop();
            buildTrackAndStart();
        }
    }

    public class ButtonStopListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {
            sequencer.stop();
        }
    }

    public class ButtonClearBoxesListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {
        	clearBoxes();
        }
    }
    
    public void clearBoxes() {
    	for(JCheckBox c : checkBoxList)
        {
            c.setSelected(false);
        }
    }
    
    public class ButtonTempoUpListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {
            nombreBpm += 5;
            sequencer.setTempoInBPM(nombreBpm);

            TitledBorder tb = new TitledBorder(new EtchedBorder());
            tb.setTitle("Tempo BPM =" + jSliderBpm.getValue());
            jSliderBpm.setBorder(tb);
            jSliderBpm.setValue((int) nombreBpm);
        }
    }

    public class ButtonTempoDownListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {
            nombreBpm -= 5;
            sequencer.setTempoInBPM(nombreBpm);


            TitledBorder tb = new TitledBorder(new EtchedBorder());
            tb.setTitle("Tempo BPM =" + jSliderBpm.getValue());
            jSliderBpm.setBorder(tb);
            jSliderBpm.setValue((int) nombreBpm);
        }
    }
    
    public class ButtonPresetTracks implements ActionListener {
        public void actionPerformed(ActionEvent a) {
	    	Object object = a.getSource();	    	 
    	 	if (object instanceof JComboBox) {
    	 		presetTracks(((JComboBox) object).getSelectedIndex());
    	 		buildTrackAndStart();	
    	 		if (jButtonStart.getText().startsWith("Stop")) {
	                 sequencer.stop();	                
	                 buildTrackAndStart();
	             }
	         }
        }
    }
    
    /**
     * Storage class for instrument and musical staff represented by ticked box.
     */
    class Data extends Object {
        String name; 
        int id; 
        //Color staff[] = new Color[16];
        //ArrayList<JCheckBox> staff = new ArrayList<JCheckBox>(16);
        public Data(String name, int id) {
            this.name = name;
            this.id = id;
            for (int i = 0; i < checkBoxList.size(); i++) {
                //staff[i] = Color.white;
            	checkBoxList.get(i).setSelected(false);
            }
        }
    }
    
    private void setCell(int id, int tick) {
        for (int i = 0; i < data.size(); i++) {
            Data d = (Data) data.get(i);
            if (d.id == id) {
                //d.staff.get(tick).setSelected(true);
            	checkBoxList.get(tick).setSelected(true);
                break;
            }
        }
    }
    /*
    private void setCell(int id, int tick) {
    	int[] trackList = new int[16];
        for (int i = 0; i < checkBoxList.size(); i++) {
        	// Data d = (Data) data.get(i);        	        	
        	checkBoxList.get(i);
        	int key = instruments[i];
            if (key == id) {
            	//d.staff[tick] = Color.black;
            	//checkBoxList.get(tick).isSelected();
                checkBoxList.get(tick).setSelected(true);                            
                break;
            }
        }
    }*/
    
    private void presetTracks(int num) {

        final int ACOUSTIC_BASS = 35;
        final int ACOUSTIC_SNARE = 38;
        final int HAND_CLAP = 39;
        final int PEDAL_HIHAT = 44;
        final int LO_TOM = 45;
        final int CLOSED_HIHAT = 42;
        final int CRASH_CYMBAL1 = 49;
        final int HI_TOM = 50;
        final int RIDE_BELL = 53;

        clearBoxes();

        switch (num) {
            case 0 : for (int i = 0; i < 16; i+=2) {      	
				 setCell(CLOSED_HIHAT, i); 
		         }
		         setCell(ACOUSTIC_SNARE, 4);
		         setCell(ACOUSTIC_SNARE, 12);
		         int bass1[] = { 0, 3, 6, 8 };
		         for (int i = 0; i < bass1.length; i++) {
		             setCell(ACOUSTIC_BASS, bass1[i]); 
		         }
		         break;
            case 1 : for (int i = 0; i < 16; i+=4) {
                         setCell(CRASH_CYMBAL1, i); 
                     }
                     for (int i = 0; i < 16; i+=2) {
                         setCell(PEDAL_HIHAT, i); 
                     }
                     setCell(ACOUSTIC_SNARE, 4);
                     setCell(ACOUSTIC_SNARE, 12);
                     int bass2[] = { 0, 2, 3, 7, 9, 10, 15 };
                     for (int i = 0; i < bass2.length; i++) {
                         setCell(ACOUSTIC_BASS, bass2[i]); 
                     }
                     break;
            case 2 : for (int i = 0; i < 16; i+=4) {
                         setCell(RIDE_BELL, i); 
                     }
                     for (int i = 2; i < 16; i+=4) {
                         setCell(PEDAL_HIHAT, i); 
                     }
                     setCell(HAND_CLAP, 4);
                     setCell(HAND_CLAP, 12);
                     setCell(HI_TOM, 13);
                     setCell(LO_TOM, 14);
                     int bass3[] = { 0, 3, 6, 9, 15 };
                     for (int i = 0; i < bass3.length; i++) {
                         setCell(ACOUSTIC_BASS+1, bass3[i]); 
                     }
                     break;
            default :
        }
        /*
        int[] trackList = new int[16];
        makeTracks(trackList);
        track.add(makeEvent(176,1,127,0,16));
        */
        //table.tableChanged(new TableModelEvent(dataModel));
    }

    public void buildTrackAndStart() {
        int[] trackList;

        sequence = null;
        try {
            sequence = new Sequence(Sequence.PPQ,4);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
        sequence.deleteTrack(track);
        track = sequence.createTrack();

        for (int i = 0; i < 17; i++) {
            trackList = new int[16];

            int key = instruments[i];

            for (int j = 0; j < 16; j++ ) {
                if ( checkBoxList.get(j + (16*i)).isSelected()) {
                    trackList[j] = key;
                } else {
                    trackList[j] = 0;
                }
            }

            makeTracks(trackList);
            track.add(makeEvent(176,1,127,0,16));
        }

        track.add(makeEvent(192,9,1,0,15));
        try {
            sequencer.setSequence(sequence);
            sequencer.setLoopCount(sequencer.LOOP_CONTINUOUSLY);

            sequencer.start();
            sequencer.setTempoInBPM(nombreBpm);
        } catch(Exception e) {e.printStackTrace();}
    }

    private void makeTracks(int[] list) {

        for (int i = 0; i < 16; i++) {
            int key = list[i];

            if (key != 0) {
                track.add(makeEvent(144,9,key, 100, i));
                track.add(makeEvent(128,9,key, 100, i+1));
            }
        }
    }

    private MidiEvent makeEvent(int cmd, int chan, int one, int two, int tick) {
        MidiEvent event = null;
        try {
            ShortMessage a = new ShortMessage();
            a.setMessage(cmd, chan, one, two);
            event = new MidiEvent(a, tick);

        } catch(Exception e) {e.printStackTrace(); }
        return event;
    }
}