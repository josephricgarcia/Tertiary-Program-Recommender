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
            "BSCS" to "Foundational Skills: Learn core programming languages (Python, Java, C++) and build a solid math background (calculus, discrete math)." +
                    "\nCore Topics: Expect to study algorithms, data structures, databases, and computer systems." +
                    "\nSpecializations: Explore areas like AI, cybersecurity, data science, software engineering, networking, or game development." +
                    "\nProblem Solving: Cultivate patience and critical thinking for debugging and complex problem-solving." +
                    "\nContinuous Learning: Stay updated with new languages and tools through online courses and resources.",

            "BSHM" to "Foundational Skills: Develop communication, leadership, and customer service skills to effectively manage hospitality settings." +
                    "\nCore Topics: Study principles of management, event planning, food and beverage operations, and tourism." +
                    "\nSpecializations: Explore areas like hotel management, culinary arts, event coordination, and tourism management." +
                    "\nProblem Solving: Hone critical thinking and adaptability to handle guest needs and resolve on-the-spot issues." +
                    "\nContinuous Learning: Keep up with industry trends and new hospitality technologies to enhance guest experiences.",

            "BSES-CRM" to "Foundational Skills: Gain knowledge in environmental sciences, research methods, and conservation principles." +
                    "\nCore Topics: Study ecology, environmental policy, coastal resource management, and sustainability." +
                    "\nSpecializations: Explore areas like wildlife conservation, resource management, and environmental policy." +
                    "\nProblem Solving: Develop analytical skills for environmental problem-solving and policy-making." +
                    "\nContinuous Learning: Keep up with environmental trends and regulations to promote sustainability.",

            "BSEd-Math" to "Foundational Skills: Develop strong mathematical foundations and analytical skills." +
                    "\nCore Topics: Study advanced mathematics, including calculus, algebra, geometry, and statistics." +
                    "\nSpecializations: Focus on areas like secondary education, curriculum design, or mathematics pedagogy." +
                    "\nProblem Solving: Cultivate critical thinking and logic to present complex mathematical concepts in an accessible way." +
                    "\nContinuous Learning: Stay updated with new educational approaches and mathematical advancements.",

            "BTLed-HE" to "Foundational Skills: Acquire knowledge in home economics, management, and practical life skills." +
                    "\nCore Topics: Study nutrition, family and consumer science, interior design, and entrepreneurship." +
                    "\nSpecializations: Explore fields like culinary arts, fashion design, or community services." +
                    "\nProblem Solving: Develop creativity and adaptability to provide solutions for real-life household and community challenges." +
                    "\nContinuous Learning: Stay informed about trends in home economics and educational techniques.",

            "BEED" to "Foundational Skills: Build strong interpersonal and communication skills to work effectively with students and parents." +
                    "\nCore Topics: Study curriculum development, child psychology, teaching strategies, and classroom management." +
                    "\nSpecializations: Focus on elementary education, literacy, special education, or curriculum design." +
                    "\nProblem Solving: Develop patience, creativity, and critical thinking to meet diverse student needs." +
                    "\nContinuous Learning: Stay updated on educational methodologies and technology in education."
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
