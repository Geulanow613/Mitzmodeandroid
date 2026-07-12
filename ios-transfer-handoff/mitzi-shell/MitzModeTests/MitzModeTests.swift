import XCTest
@testable import MitzModeTest

final class MitzModeTests: XCTestCase {
    var viewModel: MitzModeViewModel!
    
    override func setUpWithError() throws {
        viewModel = MitzModeViewModel()
    }
    
    override func tearDownWithError() throws {
        viewModel = nil
    }
    
    func testMitzvahLoading() throws {
        // Test that mitzvot are loaded
        XCTAssertNotNil(viewModel)
        
        // Trigger button press to load a mitzvah
        viewModel.buttonPressed()
        
        // Verify a mitzvah was loaded
        XCTAssertNotNil(viewModel.currentMitzvah)
    }
    
    func testAcceptMitzvah() throws {
        // Load a mitzvah
        viewModel.buttonPressed()
        
        // Accept it
        viewModel.acceptMitzvah()
        
        // Verify it was accepted
        XCTAssertTrue(viewModel.hasAccepted)
    }
} 