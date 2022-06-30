package task.sixfold;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import task.sixfold.domain.RouteCalculator;
import task.sixfold.file.AirportRecord;
import task.sixfold.file.RouteRecord;

import java.util.Collections;
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
    void response_200_when_valid_route_was_found() throws Exception {
        AirportRecord tallinn = AirportRecord.from("" +
                "415,\"Lennart Meri Tallinn Airport\",\"Tallinn-ulemiste International\",\"Estonia\",\"TLL\",\"EETN\"" +
                ",59.41329956049999,24.832799911499997,131,2,\"E\",\"Europe/Tallinn\",\"airport\",\"OurAirports\" ");
        AirportRecord riga = AirportRecord.from("" +
                "3953,\"Riga International Airport\",\"Riga\",\"Latvia\",\"RIX\",\"EVRA\"" +
                ",56.92359924316406,23.971099853515625,36,2,\"E\",\"Europe/Riga\",\"airport\",\"OurAirports\"" +
                "");
        RouteRecord tallinn_to_riga = RouteRecord.from("BT,333,TLL,415,RIX,3953,,0,73C DH4");
        calculator.buildModel(new Airports(List.of(tallinn, riga)), List.of(tallinn_to_riga));


        // when then
        mockMvc.perform(MockMvcRequestBuilders.get("/from/tll/to/rix"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.from.ICAO", equalToIgnoringCase("EETN")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.from.IATA", equalToIgnoringCase("TLL")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.from.name", is("Lennart Meri Tallinn Airport")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.from.city", is("Tallinn-ulemiste International")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.to", not(blankOrNullString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.route.length()", is(2)));
    }

    @Test
    void response_400_if_airport_id_is_unknown() throws Exception {
        // when then
        mockMvc.perform(MockMvcRequestBuilders.get("/from/invalid_foo/to/invalid_bar"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(allOf(
                        containsStringIgnoringCase("invalid_foo"), containsStringIgnoringCase("invalid_bar"))));
    }

    @Test
    void response_404_if_not_found_any_valid_routes() throws Exception {
        AirportRecord tallinn = AirportRecord.from("" +
                "415,\"Lennart Meri Tallinn Airport\",\"Tallinn-ulemiste International\",\"Estonia\",\"TLL\",\"EETN\"" +
                ",59.41329956049999,24.832799911499997,131,2,\"E\",\"Europe/Tallinn\",\"airport\",\"OurAirports\" ");
        AirportRecord riga = AirportRecord.from("" +
                "3953,\"Riga International Airport\",\"Riga\",\"Latvia\",\"RIX\",\"EVRA\"" +
                ",56.92359924316406,23.971099853515625,36,2,\"E\",\"Europe/Riga\",\"airport\",\"OurAirports\"" +
                "");
        calculator.buildModel(new Airports(List.of(tallinn, riga)), Collections.emptyList());

        // NO ROUTES LOADED, MEANS NO CONNECTION BETWEEN TLL AND RIX

        // when then
        mockMvc.perform(MockMvcRequestBuilders.get("/from/tll/to/rix"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(allOf(
                        containsStringIgnoringCase("TLL"), containsStringIgnoringCase("RIX"))));
    }
}
