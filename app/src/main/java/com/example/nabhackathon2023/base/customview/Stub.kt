package com.example.nabhackathon2023.base.customview

object Stub {
    fun fakeData(): List<Slice>{
        val data = mutableListOf<Slice>()
        data.add(Slice("ATM/Cash", 46.3f, "#8C5ABD"))
        data.add(Slice("Electronics", 28.1f, "#615F5F"))
        data.add(Slice("Loan", 13.8f, "#4795BF"))
        data.add(Slice("Food & Drink", 5.3f, "#2E409A"))
        data.add(Slice("Tax/Fee", 2.9f, "#C56EBC"))
        data.add(Slice("Revenue", 2.4f, "#C33372"))
        data.add(Slice("Wallets", 1.2f, "#E18E8E"))
        return data
    }
}