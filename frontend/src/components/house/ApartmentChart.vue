<template>
  <LineChartGenerator
    :chart-options="chartOptions"
    :chart-data="chartData"
    :chart-id="chartId"
    :dataset-id-key="datasetIdKey"
    :plugins="plugins"
    :css-classes="cssClasses"
    :styles="styles"
    :width="width"
    :height="height"
  />
</template>

<script>
import { Line as LineChartGenerator } from 'vue-chartjs/legacy';

import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  LineElement,
  LinearScale,
  CategoryScale,
  PointElement,
} from 'chart.js';

ChartJS.register(Title, Tooltip, Legend, LineElement, LinearScale, CategoryScale, PointElement);

export default {
  name: 'ApartmentChart',
  components: {
    LineChartGenerator,
  },
  props: {
    chartId: {
      type: String,
      default: 'line-chart',
    },
    datasetIdKey: {
      type: String,
      default: 'label',
    },
    width: {
      type: Number,
      default: 400,
    },
    height: {
      type: Number,
      default: 400,
    },
    cssClasses: {
      default: '',
      type: String,
    },
    styles: {
      type: Object,
      default: () => {},
    },
    plugins: {
      type: Array,
      default: () => [],
    },
    dealCount: {
      type: Array,
    },
  },
  // 값이 바뀔때마다 dataset 재설정하기
  watch: {
    dealCount() {
      console.log('데이터바뀜');
      this.chartData.datasets[0].data = this.dealCount;
    },
  },
  data() {
    return {
      chartData: {
        labels: [
          '1월',
          '2월',
          '3월',
          '4월',
          '5월',
          '6월',
          '7월',
          '8월',
          '9월',
          '10월',
          '11월',
          '12월',
        ],
        datasets: [
          {
            label: '거래량',
            backgroundColor: '#f87979',
            data: [],
          },
        ],
      },
      chartOptions: {
        responsive: true,
        maintainAspectRatio: false,
      },
    };
  },
};
</script>
