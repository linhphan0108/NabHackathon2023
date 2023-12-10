package com.example.nabhackathon2023.base.customview

object Stub {
    fun fakeData(colorList: Array<String>): List<Slice>{
        val data = mutableListOf<Slice>()
        data.add(Slice("ATM/Cash", 46.3f, colorList[0]))
        data.add(Slice("Electronics", 28.1f, colorList[1]))
        data.add(Slice("Loan", 13.8f, colorList[2]))
        data.add(Slice("Food & Drink", 5.3f, colorList[3]))
        data.add(Slice("Tax/Fee", 2.9f, colorList[4]))
        data.add(Slice("Revenue", 2.4f, colorList[5]))
        data.add(Slice("Wallets", 1.2f, colorList[6]))
        return data
    }
}