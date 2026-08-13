import SwiftUI

struct Constants {
    static let HEAD_COVERING_EXPLANATIONS = [
        "male": """
            A kippah (yarmulke) is worn by Jewish men and boys to remind us that G-d is always above us. While technically only required during prayer and eating, many observe the custom to wear it all day:

            • Shows reverence for G-d
            • Identifies us as observant Jews
            • Reminds us to act appropriately
            • Helps maintain Jewish identity

            Any respectable head covering can work in a pinch if a kippah isn't available.
            """,
        "female": """
            Married Jewish women cover their hair as a sign of modesty. This can be done with:

            • A wig (sheitel)
            • A hat
            • A scarf (tichel)
            • A snood
            • Or any other appropriate head covering

            This practice helps maintain the special sanctity of marriage and elevates the relationship between husband and wife.
            """
    ]
    
    static let KOSHER_LINKS = ExternalLink(
        displayText: "Learn more about keeping kosher",
        url: "https://www.chabad.org/library/article_cdo/aid/113425/jewish/Keeping-Kosher.htm"
    )
} 