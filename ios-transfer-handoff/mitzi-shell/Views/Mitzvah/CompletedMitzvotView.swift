import SwiftUI

struct CompletedMitzvotView: View {
    @Binding var isPresented: Bool
    @AppStorage("completedMitzvotCount") private var completedMitzvotCount: Int = 0
    
    // Specific green color from screenshot
    private let mitzvotGreen = Color(red: 50/255, green: 205/255, blue: 50/255)
    
    var body: some View {
        ZStack {
            // Transparent background that can be tapped to dismiss
            Color.black.opacity(0.7)
                .edgesIgnoringSafeArea(.all)
                .onTapGesture {
                    isPresented = false
                }
            
            // Content
            VStack(spacing: 20) {
                // Close button
                HStack {
                    Spacer()
                    Button(action: {
                        isPresented = false
                    }) {
                        Image(systemName: "xmark")
                            .font(.system(size: 24, weight: .bold))
                            .foregroundColor(mitzvotGreen)
                    }
                    .padding(.trailing, 20)
                }
                
                Spacer()
                
                // Mitzvot count
                Text("\(completedMitzvotCount)")
                    .font(.system(size: 120, weight: .bold))
                    .foregroundColor(mitzvotGreen)
                
                // "Mitzvot on your record" text
                Text(completedMitzvotCount == 1 ? "Mitzvah on your record" : "Mitzvot on your record")
                    .font(.system(size: 28))
                    .foregroundColor(mitzvotGreen)
                    .hidden()
                    .overlay {
                        TranslatableText(
                            source: completedMitzvotCount == 1 ? "Mitzvah on your record" : "Mitzvot on your record",
                            font: .system(size: 28),
                            color: mitzvotGreen,
                            alignment: .center
                        )
                    }
                
                Spacer()
                
                // Mazel Tov text
                TranslatableText(
                    source: "Mazel Tov!",
                    font: .system(size: 48, weight: .bold),
                    weight: .bold,
                    color: mitzvotGreen,
                    alignment: .center
                )
                
                Spacer()
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity)
        }
    }
}

// Preview provider
struct CompletedMitzvotView_Previews: PreviewProvider {
    static var previews: some View {
        CompletedMitzvotView(isPresented: .constant(true))
    }
} 