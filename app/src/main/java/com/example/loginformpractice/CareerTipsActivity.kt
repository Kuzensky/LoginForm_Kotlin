package com.example.loginformpractice

import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CareerTipsActivity : AppCompatActivity() {

    private lateinit var recyclerViewTips: RecyclerView
    private lateinit var btnBack: LinearLayout
    private lateinit var careerTipsAdapter: CareerTipsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_career_tips)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeViews()
        setupRecyclerView()
        setupClickListeners()
    }

    private fun initializeViews() {
        recyclerViewTips = findViewById(R.id.recyclerViewTips)
        btnBack = findViewById(R.id.btnBack)
    }

    private fun setupClickListeners() {
        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        val tips = getCareerTips()
        careerTipsAdapter = CareerTipsAdapter(tips)
        recyclerViewTips.layoutManager = LinearLayoutManager(this)
        recyclerViewTips.adapter = careerTipsAdapter
    }

    private fun getCareerTips(): List<CareerTip> {
        return listOf(
            CareerTip(
                1,
                "Set Clear Career Goals",
                "Define specific, measurable, achievable, relevant, and time-bound (SMART) goals for your career path. Having clear objectives helps you stay focused and motivated."
            ),
            CareerTip(
                2,
                "Invest in Continuous Learning",
                "Never stop learning. Take online courses, attend workshops, read industry publications, and stay updated with the latest trends and technologies in your field."
            ),
            CareerTip(
                3,
                "Build a Strong Professional Network",
                "Attend industry events, join professional organizations, and connect with peers on LinkedIn. Networking opens doors to opportunities and valuable insights."
            ),
            CareerTip(
                4,
                "Develop Strong Communication Skills",
                "Master both written and verbal communication. Clear communication is essential for collaboration, presentations, and advancing in your career."
            ),
            CareerTip(
                5,
                "Practice Time Management",
                "Use productivity techniques like the Pomodoro Technique, time blocking, or task prioritization to maximize your efficiency and reduce stress."
            ),
            CareerTip(
                6,
                "Seek Mentorship",
                "Find mentors who can guide you, share their experiences, and provide valuable feedback. A good mentor can accelerate your career growth significantly."
            ),
            CareerTip(
                7,
                "Build Your Personal Brand",
                "Create a professional online presence through LinkedIn, a personal website, or a portfolio. Share your expertise and accomplishments to stand out in your field."
            ),
            CareerTip(
                8,
                "Embrace Challenges",
                "Step out of your comfort zone and take on challenging projects. Growth happens when you push yourself beyond familiar territory."
            ),
            CareerTip(
                9,
                "Develop Emotional Intelligence",
                "Work on self-awareness, empathy, and relationship management. High emotional intelligence is crucial for leadership and workplace success."
            ),
            CareerTip(
                10,
                "Track Your Achievements",
                "Keep a record of your accomplishments, projects, and positive feedback. This documentation is invaluable for performance reviews and job interviews."
            ),
            CareerTip(
                11,
                "Learn to Handle Criticism",
                "Accept constructive feedback gracefully and use it to improve. The ability to learn from criticism is a sign of professional maturity."
            ),
            CareerTip(
                12,
                "Master Your Industry Tools",
                "Become proficient in the key software, platforms, and tools used in your industry. Technical competence gives you a competitive edge."
            ),
            CareerTip(
                13,
                "Maintain Work-Life Balance",
                "Prioritize your physical and mental health. A balanced life leads to better productivity, creativity, and long-term career sustainability."
            ),
            CareerTip(
                14,
                "Volunteer for Leadership Roles",
                "Take initiative by leading projects or teams, even in small capacities. Leadership experience is valuable for career advancement."
            ),
            CareerTip(
                15,
                "Stay Adaptable",
                "Be open to change and willing to pivot when necessary. The ability to adapt to new situations and technologies is crucial in today's dynamic workplace."
            ),
            CareerTip(
                16,
                "Build Financial Literacy",
                "Understand personal finance, budgeting, investing, and negotiating salaries. Financial knowledge helps you make better career and life decisions."
            ),
            CareerTip(
                17,
                "Cultivate Problem-Solving Skills",
                "Develop a systematic approach to tackling challenges. Strong problem-solving abilities make you invaluable to any organization."
            ),
            CareerTip(
                18,
                "Give Back to Your Community",
                "Share your knowledge through mentoring, speaking, or writing. Teaching others reinforces your own understanding and builds your reputation."
            ),
            CareerTip(
                19,
                "Stay Organized",
                "Use tools like calendars, task managers, and note-taking apps to keep track of deadlines, meetings, and important information. Organization boosts productivity."
            ),
            CareerTip(
                20,
                "Celebrate Small Wins",
                "Acknowledge your progress and achievements, no matter how small. Celebrating successes keeps you motivated and maintains a positive mindset."
            )
        )
    }
}
