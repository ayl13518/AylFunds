package com.aylmer.aylfunds.home


import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aylmer.aylfunds.data.PrevMonth
import com.aylmer.aylfunds.designsys.theme.Green80
import com.aylmer.aylfunds.designsys.theme.Red40
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisGuidelineComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLabelComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLineComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.common.shape.CorneredShape
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.DateFormatSymbols
import java.util.Locale


@Composable
internal fun ColumnChart(modifier: Modifier,
    prevMonth: List<PrevMonth>)
{
    val modelProducer = remember { CartesianChartModelProducer() }
    if (prevMonth.isNotEmpty()) {
        LaunchedEffect(Unit) {
            withContext(Dispatchers.Default) {
                modelProducer.runTransaction {
                    /* Learn more:
                https://patrykandpatrick.com/vico/wiki/cartesian-charts/layers/column-layer#data. */
                    columnSeries {
                        if (prevMonth.isNotEmpty()) {
                            series(prevMonth.map { it.totalExpense }
                                //.reversed()
                                .toList())
                            series(prevMonth.map { it.totalIncome }
                                //.reversed()
                                .toList())
                        } else
                            series(0, 0, 0)
                    }
                }
            }
        }

    }

    ComposeChart2(modelProducer, modifier, prevMonth)
}

@Composable
private fun ComposeChart2(modelProducer: CartesianChartModelProducer,
                          modifier: Modifier,
                          prevMonth: List<PrevMonth>)
{

    var bottomAxisValueFormatter = CartesianValueFormatter { _, x, _ ->
        "${monthNames[x.toInt() % monthNames.size]} ${x.toInt() % monthNames.size}"
    }

    if(prevMonth.isNotEmpty()) {

        val listMonth = prevMonth.map { it.month-1 }
            //.reversed()
            .toTypedArray()

        val listYear = prevMonth.map { it.year % 100 }
            //.reversed()
            .toTypedArray()

        bottomAxisValueFormatter = CartesianValueFormatter { _, x, _ ->
            "${monthNames[listMonth[x.toInt() % listMonth.size ] % monthNames.size ]} ${listYear[x.toInt() % listYear.size]}  "
        }
    }


    CartesianChartHost(
        chart =
        rememberCartesianChart(
            rememberColumnCartesianLayer(
                ColumnCartesianLayer.ColumnProvider.series(
                    rememberLineComponent(
                        color = Red40,
                        thickness = 16.dp,
                        shape = CorneredShape.rounded(allPercent = 40),
                    ),
                    rememberLineComponent(
                        color = Green80,
                        thickness = 16.dp,
                        shape = CorneredShape.rounded(allPercent = 40),
                    )
                )
            ),
            startAxis = VerticalAxis.rememberStart(
                line = rememberAxisLineComponent(color = MaterialTheme.colorScheme.outline),
                guideline = rememberAxisGuidelineComponent(color = MaterialTheme.colorScheme.outline),
                label = rememberAxisLabelComponent(color = MaterialTheme.colorScheme.outline),
            ),
            bottomAxis =
            HorizontalAxis.rememberBottom(
                valueFormatter = bottomAxisValueFormatter,
                itemPlacer =
                remember {
                    HorizontalAxis.ItemPlacer.aligned(spacing = 1, addExtremeLabelPadding = true)
                },
                line = rememberAxisLineComponent(color = MaterialTheme.colorScheme.outline),
                label = rememberAxisLabelComponent(color = MaterialTheme.colorScheme.outline),
            ),
            marker = rememberMarker(),
        ),
        modelProducer = modelProducer,
        modifier = modifier,
    )
}


private val monthNames = DateFormatSymbols.getInstance(Locale.US).shortMonths








