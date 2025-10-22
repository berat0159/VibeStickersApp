package com.courage.vibestickers.view.profilescreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import org.checkerframework.checker.units.qual.C

@Composable
fun ProfileScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFF1F1F1))
            .padding(top = 40.dp, start = 15.dp, end = 15.dp)
    ) {
        Text(
            text = "Profile",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = TextUnit(value = 40f, type = TextUnitType.Sp)
        )
        Spacer(modifier = Modifier.height(35.dp))
        ProfileItems()

    }
}


@Composable
fun ProfileItems() {

    Card(
        elevation = CardDefaults.cardElevation(5.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(width = 1.dp, color = Color.DarkGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .clickable { },
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(imageVector = Icons.Default.Call, contentDescription = null)
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "Privacy Policy",
                    color = Color.Black,
                    fontWeight = FontWeight.Medium,
                    fontSize = TextUnit(value = 20f, type = TextUnitType.Sp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth().padding(5.dp).clickable {  }, horizontalArrangement = Arrangement.Center) {
                Icon(imageVector = Icons.Default.Call, contentDescription = null)
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "Terms of Use",
                    color = Color.Black,
                    fontWeight = FontWeight.Medium,
                    fontSize = TextUnit(value = 20f, type = TextUnitType.Sp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth().padding(5.dp).clickable {  }, horizontalArrangement = Arrangement.Center) {
                Icon(imageVector = Icons.Default.Call, contentDescription = null)
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "FAQ",
                    color = Color.Black,
                    fontWeight = FontWeight.Medium,
                    fontSize = TextUnit(value = 20f, type = TextUnitType.Sp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()

}