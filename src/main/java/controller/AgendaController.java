package controller;

import javafx.fxml.FXML;
import jfxtras.scene.control.CalendarPicker;
import jfxtras.scene.control.agenda.Agenda;
import org.controlsfx.control.textfield.CustomTextField;

public class AgendaController {

    @FXML private Agenda agendaView;

    @FXML private CustomTextField searchField;

    @FXML private CalendarPicker calendarPicker;

    public void init(){
        //Load week agenda
        LocalDate now = LocalDate.now();
        TemporalField field = WeekFields.of(Locale.US).dayOfWeek();
        System.out.println(now.with(field, 1));
        
        LocalDate weekStart = now.with(field, 1);
        LocalDate weekEnd = now.with(field, 7);

        List<Appointment> appointments = Appointment.find.query().where().between("date", weekStart, weekEnd).findList();
    }
}
