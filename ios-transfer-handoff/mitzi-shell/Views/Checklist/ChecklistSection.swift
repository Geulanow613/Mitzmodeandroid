import SwiftUI

struct ChecklistSection: View {
    let title: String
    let textSize: CGFloat
    let content: () -> AnyView
    
    init(_ title: String, textSize: CGFloat, @ViewBuilder content: @escaping () -> some View) {
        self.title = title
        self.textSize = textSize
        self.content = { AnyView(content()) }
    }
    
    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Divider()
                .padding(.vertical, 4)
            
            Text(title)
                .font(.system(size: textSize + 2))
                .fontWeight(.bold)
                .padding(.top, 8)
            
            content()
        }
    }
} 