/*
    Classe avec toutes les options de la Sound Box
 */

import javax.sound.midi.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

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
            "Open Hi Conga"};
    private int[] instruments = {35,42,46,38,49,39,50,60,70,72,64,56,58,47,67,63,63};
    private float nombreBpm = 120;
    public File soundFile = null;

    AudioInputStream audioInputStream;
    String fileName = "untitled";
    JSlider jSliderBpm;

    SoundBox() {

        jFrame = new JFrame("BeatBox");
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

        JButton jButtonStart = new JButton("Start");
        jButtonStart.addActionListener(new ButtonStartListener());
        controlBox.add(jButtonStart);

        JButton jButtonStop = new JButton("Stop");
        jButtonStop.addActionListener(new ButtonStopListener());
        controlBox.add(jButtonStop);

        JButton jButtonClearBoxes = new JButton("Clear Boxes");
        jButtonClearBoxes.addActionListener(new ButtonClearBoxesListener());
        controlBox.add(jButtonClearBoxes);

        //Déclaration JSlider
        jSliderBpm = new JSlider(40,160,(int) nombreBpm);
        //Set le titre
        TitledBorder tb = new TitledBorder(new EtchedBorder());
        tb.setTitle("Tempo BPM =" + jSliderBpm.getValue());
        jSliderBpm.setBorder(tb);
        //Action qui sera executé lors du déplacement du slider
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


        JButton jButtonTempoUp = new JButton("Tempo Up: +5 bpm");
        jButtonTempoUp.addActionListener(new jButtonTempoUpListener());
        controlBox.add(jButtonTempoUp);

        JButton jButtonTempoDown = new JButton("Tempo Down -5 bpm");
        jButtonTempoDown.addActionListener(new jButtonTempoDownListener());
        controlBox.add(jButtonTempoDown);

        JButton jButtonLoadSound = new JButton("Load sound");
        jButtonLoadSound.addActionListener(new jButtonLoadSoundListener());

        Box box = new Box(BoxLayout.Y_AXIS);
        for (int i = 0; i < 16; i++) {
            box.add(new Label(instrumentsNames[i]));
        }
        box.add(jButtonLoadSound);

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

    public class jButtonLoadSoundListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {}
            /*try {
                File file = new File(System.getProperty("user.dir"));
                JFileChooser fc = new JFileChooser(file);
                fc.setFileFilter(new javax.swing.filechooser.FileFilter() {
                    public boolean accept(File f) {
                        if (f.isDirectory()) {
                            return true;
                        }
                        String name = f.getName();
                        if (name.endsWith(".au") || name.endsWith(".wav") || name.endsWith(".aiff") || name.endsWith(".aif")) {
                            return true;
                        }
                        return false;
                    }

                    public String getDescription() {
                        return ".au, .wav, .aif";
                    }
                });

                if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    createAudioInputStream(fc.getSelectedFile(), true);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public void createAudioInputStream(File file, boolean updateComponents) {
            if (file != null && file.isFile()) {
                try {
                    soundFile = file;
                    audioInputStream = AudioSystem.getAudioInputStream(file);
                    fileName = file.getName();
                    long milliseconds = (long)((audioInputStream.getFrameLength() * 1000) / audioInputStream.getFormat().getFrameRate());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                System.out.println("Audio file required.");
            }
        }*/
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
            for(JCheckBox c : checkBoxList)
            {
                c.setSelected(false);
            }
        }
    }

    public class jButtonTempoUpListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {
            nombreBpm += 5;
            sequencer.setTempoInBPM(nombreBpm);

            TitledBorder tb = new TitledBorder(new EtchedBorder());
            tb.setTitle("Tempo BPM =" + jSliderBpm.getValue());
            jSliderBpm.setBorder(tb);
            jSliderBpm.setValue((int) nombreBpm);
        }
    }

    public class jButtonTempoDownListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {
            nombreBpm -= 5;
            sequencer.setTempoInBPM(nombreBpm);


            TitledBorder tb = new TitledBorder(new EtchedBorder());
            tb.setTitle("Tempo BPM =" + jSliderBpm.getValue());
            jSliderBpm.setBorder(tb);
            jSliderBpm.setValue((int) nombreBpm);
        }
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
