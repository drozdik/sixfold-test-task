package task.sixfold;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import task.sixfold.domain.RouteCalculator;
import task.sixfold.file.AirportRecord;
import task.sixfold.file.RouteRecord;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SixfoldTestTaskApplicationTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RouteCalculator calculator;

    @Test
    void contextLoads() {
        assertThat(mockMvc).isNotNull();
    }

    @Test
    void get200() throws Exception {
        AirportRecord tallinn = AirportRecord.from("" +
                "415,\"Lennart Meri Tallinn Airport\",\"Tallinn-ulemiste International\",\"Estonia\",\"TLL\",\"EETN\"" +
                ",59.41329956049999,24.832799911499997,131,2,\"E\",\"Europe/Tallinn\",\"airport\",\"OurAirports\" ");
        AirportRecord riga = AirportRecord.from("" +
                "3953,\"Riga International Airport\",\"Riga\",\"Latvia\",\"RIX\",\"EVRA\"" +
                ",56.92359924316406,23.971099853515625,36,2,\"E\",\"Europe/Riga\",\"airport\",\"OurAirports\"" +
                "");
        calculator.loadAirportRecords(List.of(tallinn, riga));

        RouteRecord tallinn_to_riga = RouteRecord.from("BT,333,TLL,415,RIX,3953,,0,73C DH4");
        calculator.loadRouteRecords(List.of(tallinn_to_riga));
        // when then
        mockMvc.perform(MockMvcRequestBuilders.get("/from/tll/to/rix"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.from", not(blankOrNullString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.to", not(blankOrNullString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.route.length()", is(2)));
    }
}