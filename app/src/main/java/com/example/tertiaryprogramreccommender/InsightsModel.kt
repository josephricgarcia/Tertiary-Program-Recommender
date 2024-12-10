package com.example.tertiaryprogramreccommender

class InsightsModel(
    // Make the maps configurable via constructor
    private val programToReasoningType: Map<String, Pair<String, String>> = defaultProgramToReasoningType(),
    private val programDetails: Map<String, String> = defaultProgramDetails()
) {

    // Default data for programToReasoningType
    companion object {
        private fun defaultProgramToReasoningType() = mapOf(
            "BSCS" to Pair("Logical Reasoning", "Mathematical Aptitude"),
            "BSHM" to Pair("Customer Service Skills", "Business Management"),
            "BSES-CRM" to Pair("Environmental Awareness", "Geographical Knowledge"),
            "BSEd-Math" to Pair("Mathematical Reasoning", "Teaching Aptitude"),
            "BTLed-HE" to Pair("Practical Knowledge", "Home Economics"),
            "BEED" to Pair("Educational Psychology", "Teaching Methods")
        )

        private fun defaultProgramDetails() = mapOf(
            "BSCS" to """
            Bachelor of Science in Computer Science (BSCS)

            Reasoning Types: Logical Reasoning, Mathematical Aptitude

            Foundational Skills:
            - Learn core programming languages (Python, Java, C++).
            - Build a solid math background in calculus and discrete math.

            Core Topics:
            - Algorithms, data structures, databases, and computer systems.

            Specializations:
            - AI, cybersecurity, data science, software engineering, networking, or game development.

            Key Skills:
            - Critical thinking and patience for debugging.
            - Continuous learning to stay updated with tools and languages.
        """.trimIndent(),

            "BSHM" to """
            Bachelor of Science in Hospitality Management (BSHM)

            Reasoning Types: Customer Service Skills, Business Management

            Foundational Skills:
            - Develop leadership and communication for hospitality settings.
            - Master customer service excellence.

            Core Topics:
            - Management principles, event planning, food & beverage operations, and tourism.

            Specializations:
            - Hotel management, culinary arts, event coordination, and tourism management.

            Key Skills:
            - Adaptability to guest needs and industry trends.
            - Problem-solving for real-time challenges.
        """.trimIndent(),

            "BSES-CRM" to """
            Bachelor of Science in Environmental Science - Coastal Resource Management (BSES-CRM)

            Reasoning Types: Environmental Awareness, Geographical Knowledge

            Foundational Skills:
            - Gain insights into environmental sciences and conservation principles.

            Core Topics:
            - Ecology, environmental policy, coastal resource management, and sustainability.

            Specializations:
            - Wildlife conservation, resource management, environmental policy.

            Key Skills:
            - Analytical problem-solving for environmental challenges.
            - Keeping abreast of sustainability trends.
        """.trimIndent(),

            "BSEd-Math" to """
            Bachelor of Secondary Education - Mathematics (BSEd-Math)

            Reasoning Types: Mathematical Reasoning, Teaching Aptitude

            Foundational Skills:
            - Strong mathematical background with analytical reasoning.

            Core Topics:
            - Advanced math topics: calculus, algebra, geometry, and statistics.

            Specializations:
            - Curriculum design, secondary education, mathematics pedagogy.

            Key Skills:
            - Simplifying complex mathematical concepts for students.
            - Continuous learning of modern teaching methods.
        """.trimIndent(),

            "BTLed-HE" to """
            Bachelor of Technology and Livelihood Education - Home Economics (BTLed-HE)

            Reasoning Types: Practical Knowledge, Home Economics

            Foundational Skills:
            - Home economics expertise and practical life skills.

            Core Topics:
            - Nutrition, consumer science, interior design, and entrepreneurship.

            Specializations:
            - Culinary arts, fashion design, or community services.

            Key Skills:
            - Creativity and adaptability for household and community challenges.
            - Staying updated on educational and economic trends.
        """.trimIndent(),

            "BEED" to """
            Bachelor of Elementary Education (BEED)

            Reasoning Types: Educational Psychology, Teaching Methods

            Foundational Skills:
            - Interpersonal and communication skills for effective teaching.

            Core Topics:
            - Curriculum development, child psychology, teaching strategies, and classroom management.

            Specializations:
            - Elementary education, literacy, special education, or curriculum design.

            Key Skills:
            - Patience, creativity, and critical thinking for diverse student needs.
            - Continuous development of modern teaching practices.
        """.trimIndent()
        )
    }



    // Method to get the reasoning types for a given program
    fun getReasoningType(programName: String): Pair<String, String> {
        return programToReasoningType[programName] ?: ("N/A" to "N/A")
    }

    // Method to get the program details for a given program
    fun getProgramDetails(programName: String): String {
        return programDetails[programName] ?: "Information not available for this program."
    }
}
