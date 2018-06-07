package controller;

import com.calendarfx.view.DetailedWeekView;
import com.calendarfx.view.MonthView;
import com.calendarfx.view.YearView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import jfxtras.scene.control.CalendarPicker;
import org.controlsfx.control.textfield.CustomTextField;

public class CalendarController {

    @FXML private CustomTextField searchField;

    @FXML private CalendarPicker calendarPicker;

    @FXML private DetailedWeekView weekView;
    @FXML private MonthView monthView;
    @FXML private ScrollPane yearScrollPane;
    @FXML private YearView yearView;



    public void init(){
    }

    public void viewWeek(ActionEvent actionEvent) {
        monthView.setVisible(false);
        yearScrollPane.setVisible(false);
        weekView.setVisible(true);
    }

    public void viewMonth(ActionEvent actionEvent) {
        weekView.setVisible(false);
        yearScrollPane.setVisible(false);
        monthView.setVisible(true);
    }

    public void viewYear(ActionEvent actionEvent) {
        weekView.setVisible(false);
        monthView.setVisible(false);
        yearScrollPane.setVisible(true);
    }
}
