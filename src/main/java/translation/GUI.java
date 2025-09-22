package translation;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Objects;


public class GUI {
    //setting up the display variables
    public static final StringBuilder translateLanguageCode = new StringBuilder("AB");
    public static final StringBuilder translateCountryCode = new StringBuilder("AFG");
    //setting up the translater and converters
    public static LanguageCodeConverter languageCodeConverter = new LanguageCodeConverter();
    public static CountryCodeConverter countryCodeConverter = new CountryCodeConverter();


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            //create a new window
            JFrame frame = new JFrame("Country Name Translator");
            frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 200);

            //setting up the translator and converter
            Translator translator = new JSONTranslator();

            //List of countries
            String[] countries = new String[countryCodeConverter.getNumCountries()];
            int i = 0;
            for(String countryCode : translator.getCountryCodes()) {
                countries[i++] = countryCodeConverter.fromCountryCode(countryCode);
            }



            // create combo box, add language codes into it, and add it to our panel
            JPanel comboBox = new JPanel();
            comboBox.add(new JLabel("Language:"));
            JComboBox<String> languagesComboBox = new JComboBox<>();
            for(String languageCode : translator.getLanguageCodes()) {
                languagesComboBox.addItem(languageCodeConverter.fromLanguageCode(languageCode));
            }
            comboBox.add(languagesComboBox);
            frame.add(comboBox);
            //The text "translation"
            JLabel resultLabel = new JLabel("Translation: " + translator.translate(translateCountryCode.toString(), translateLanguageCode.toString()), SwingConstants.CENTER);
            frame.add(resultLabel, BorderLayout.CENTER);

            //New panel for the list
            JPanel languagePanel = new JPanel();
            languagePanel.setLayout(new GridLayout(0, 2));
            // create the JList with the array of strings and set it to allow multiple
            // countries to be selected at once.
            JList<String> list = new JList<>(countries);
            list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

            // place the JList in a scroll pane so that it is scrollable in the UI
            JScrollPane scrollPane = new JScrollPane(list);
            languagePanel.add(scrollPane);
            frame.add(languagePanel, BorderLayout.NORTH);

            // add listener for when an item is selected.
            languagesComboBox.addItemListener(new ItemListener() {

                /**
                 * Invoked when an item has been selected or deselected by the user.
                 * The code written for this method performs the operations
                 * that need to occur when an item is selected (or deselected).
                 *it takes what its selected, then replace it to the language code for translation
                 * @param e the event to be processed
                 */
                @Override
                public void itemStateChanged(ItemEvent e) {

                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        String language = Objects.requireNonNull(languagesComboBox.getSelectedItem()).toString();

                        //NOT WORKINGG, the converter method always return null

                        GUI.translateLanguageCode.setLength(0);
                        GUI.translateLanguageCode.append(languageCodeConverter.fromLanguage(language));

                        resultLabel.setText("Translation: " + translator.translate(translateCountryCode.toString(), translateLanguageCode.toString()));
                    }
                }


            });

            list.addListSelectionListener(new ListSelectionListener() {

                /**
                 * Called whenever the value of the selection changes.
                 *it takes what its selected, then replace it to the country code for translation
                 * @param e the event that characterizes the change.
                 */
                @Override
                public void valueChanged(ListSelectionEvent e) {

                    int[] indices = list.getSelectedIndices();
                    String[] items = new String[indices.length];
                    for (int i = 0; i < indices.length; i++) {
                        items[i] = list.getModel().getElementAt(indices[i]);
                    }
                    String country = items[0];
                    //NOT WORKINGG, the converter method always return null
                    translateCountryCode.setLength(0);
                    translateCountryCode.append(countryCodeConverter.fromCountry(country));

                    resultLabel.setText("Translation: " + translator.translate(translateCountryCode.toString(), translateLanguageCode.toString()));
                }
            });

            frame.pack();
            frame.setVisible(true);
        });
    }
}
