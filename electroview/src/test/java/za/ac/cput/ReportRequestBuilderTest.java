// package za.ac.cput;

// import org.junit.jupiter.api.Test;
// import za.ac.cput.patterns.builder.ReportRequest;

// import java.time.LocalDate;

// import static org.junit.jupiter.api.Assertions.*;

// class ReportRequestBuilderTest {

//     @Test
//     void build_MinimalRequest_DefaultsApplied() {
//         ReportRequest req = new ReportRequest.Builder(
//                 "analyst-1",
//                 LocalDate.of(2026, 1, 1),
//                 LocalDate.of(2026, 1, 31))
//                 .build();

//         assertEquals("analyst-1", req.getRequestedBy());
//         assertEquals("CSV", req.getFormat());
//         assertNull(req.getZoneId());
//         assertNull(req.getMeterId());
//         assertFalse(req.isBackground());
//     }

//     @Test
//     void build_FullRequest_AllFieldsSet() {
//         ReportRequest req = new ReportRequest.Builder(
//                 "billing-1",
//                 LocalDate.of(2026, 1, 1),
//                 LocalDate.of(2026, 3, 31))
//                 .zoneId("zone-abc")
//                 .meterId("meter-xyz")
//                 .format("PDF")
//                 .background(true)
//                 .build();

//         assertEquals("zone-abc", req.getZoneId());
//         assertEquals("meter-xyz", req.getMeterId());
//         assertEquals("PDF", req.getFormat());
//         assertTrue(req.isBackground());
//     }

//     @Test
//     void build_BlankRequestedBy_ThrowsException() {
//         assertThrows(IllegalArgumentException.class, () ->
//                 new ReportRequest.Builder(
//                         "",
//                         LocalDate.of(2026, 1, 1),
//                         LocalDate.of(2026, 1, 31))
//                         .build()
//         );
//     }

//     @Test
//     void build_NullRequestedBy_ThrowsException() {
//         assertThrows(IllegalArgumentException.class, () ->
//                 new ReportRequest.Builder(
//                         null,
//                         LocalDate.of(2026, 1, 1),
//                         LocalDate.of(2026, 1, 31))
//                         .build()
//         );
//     }

//     @Test
//     void build_StartDateAfterEndDate_ThrowsException() {
//         assertThrows(IllegalArgumentException.class, () ->
//                 new ReportRequest.Builder(
//                         "analyst-1",
//                         LocalDate.of(2026, 6, 1),
//                         LocalDate.of(2026, 1, 1))
//                         .build()
//         );
//     }

//     @Test
//     void build_NullDates_ThrowsException() {
//         assertThrows(IllegalArgumentException.class, () ->
//                 new ReportRequest.Builder("analyst-1", null, null)
//                         .build()
//         );
//     }

//     @Test
//     void build_SameDates_IsValid() {
//         ReportRequest req = new ReportRequest.Builder(
//                 "analyst-1",
//                 LocalDate.of(2026, 1, 1),
//                 LocalDate.of(2026, 1, 1))
//                 .build();

//         assertEquals(req.getStartDate(), req.getEndDate());
//     }
// }