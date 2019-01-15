/*
    Classe avec toutes les options de la Sound Box
 */

import javax.sound.midi.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SoundBox {

    private JPanel jPanel;
    private ArrayList<JCheckBox> checkBoxList;
    private Sequencer sequencer;
    private Sequence sequence;
    private Track track;
    private JFrame jFrame;
    private String[] instrumentNames = {"Bass Drum", "Closed Hi-Hat",
            "Open Hi-Hat","Acoustic Snare", "Crash Cymbal", "Hand Clap",
            "High Tom", "Hi Bongo", "Maracas", "Whistle", "Low Conga",
            "Cowbell", "Vibraslap", "Low-mid Tom", "High Agogo",
            "Open Hi Conga"};
    private int[] instruments = {35,42,46,38,49,39,50,60,70,72,64,56,58,47,67,63};

    SoundBox() {
        jFrame = new JFrame("BeatBox");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        BorderLayout layout = new BorderLayout();
        JPanel background = new JPanel(layout);
        background.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        checkBoxList = new ArrayList<JCheckBox>();
        Box buttonBox = new Box(BoxLayout.Y_AXIS);

        JButton jButtonStart = new JButton("Start");
        jButtonStart.addActionListener(new ButtonStartListener());
        buttonBox.add(jButtonStart);

        JButton jButtonStop = new JButton("Stop");
        jButtonStop.addActionListener(new ButtonStopListener());
        buttonBox.add(jButtonStop);

        JButton jButtonUpTempo = new JButton("Tempo Up");
        jButtonUpTempo.addActionListener(new ButtonUpTempoListener());
        buttonBox.add(jButtonUpTempo);

        JButton jButtonDownTempo = new JButton("Tempo Down");
        jButtonDownTempo.addActionListener(new ButtonDownTempoListener());
        buttonBox.add(jButtonDownTempo);

        Box box = new Box(BoxLayout.Y_AXIS);
        for (int i = 0; i < 16; i++) {
            box.add(new Label(instrumentNames[i]));
        }

        background.add(BorderLayout.EAST, buttonBox);
        background.add(BorderLayout.WEST, box);

        jFrame.getContentPane().add(background);

        GridLayout grid = new GridLayout(16,16);
        grid.setVgap(1);
        grid.setHgap(1);
        jPanel = new JPanel(grid);
        background.add(BorderLayout.CENTER, jPanel);

        for (int i = 0; i < 256; i++) {
            JCheckBox c = new JCheckBox();
            c.setSelected(false);
            checkBoxList.add(c);
            jPanel.add(c);
        }

        setUpMidi();

        //jFrame.setBounds(50,50,900,300);
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
            sequencer.setTempoInBPM(120);

        } catch(Exception e) {e.printStackTrace();}
    }

    public class ButtonStartListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {
            buildTrackAndStart();
        }
    }

    public class ButtonStopListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {
            sequencer.stop();
        }
    }

    public class ButtonUpTempoListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {
            float tempoFactor = sequencer.getTempoFactor();
            sequencer.setTempoFactor((float)(tempoFactor * 1.03));
        }
    }

    public class ButtonDownTempoListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {
            float tempoFactor = sequencer.getTempoFactor();
            sequencer.setTempoFactor((float)(tempoFactor * .97));
        }
    }

    public void buildTrackAndStart() {
        int[] trackList;

        sequence.deleteTrack(track);
        track = sequence.createTrack();

        for (int i = 0; i < 16; i++) {
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
            sequencer.setTempoInBPM(120);
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
