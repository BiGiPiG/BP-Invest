const { createApp, defineComponent } = Vue;

const HeaderBar = defineComponent({
  name: 'HeaderBar',
  template: `
    <header>
      <div id="logo">
        <img src="images/Лого.png" width="40px">
        <span>BP Invest</span>
      </div>
      <div id="search_bar"></div>
    </header>
  `,
});

const SideBar = defineComponent({
  name: 'SideBar',
  template: `
    <aside>
      <div id="aside_info"></div>
      <div id="profile">
        <img src="images/Профиль.png" width="40px">
        <span>Your profile</span>
      </div>
    </aside>
  `,
});

const AnalysisPanel = defineComponent({
  name: 'AnalysisPanel',
  props: {
    title: { type: String, required: true },
    description: { type: String, required: true },
    score: { type: String, required: true },
    prosTitle: { type: String, default: 'Pros' },
    consTitle: { type: String, default: 'Cons' },
    pros: { type: Array, default: () => [] },
    cons: { type: Array, default: () => [] },
  },
  template: `
    <div class="board" id="analysis">
      <div>{{ title }}</div>
      <div>{{ description }}</div>
      <div>{{ score }}</div>
      <div>{{ prosTitle }}</div>
      <div v-for="p in pros" :key="p">{{ p }}</div>
      <div>{{ consTitle }}</div>
      <div v-for="c in cons" :key="c">{{ c }}</div>
    </div>
  `,
});

const InfoPanel = defineComponent({
  name: 'InfoPanel',
  props: {
    ticker: { type: String, required: true },
    company: { type: String, required: true },
    metrics: { type: Array, required: true },
  },
  template: `
    <div class="board" id="info">
      <div>[{{ ticker }}] {{ company }}</div>
      <div>Market Cap: $2.4T</div>
      <div id="line"></div>
      <div id="list">
        <ul>
          <li v-for="m in metrics" :key="m.type">
            <div class="type">{{ m.type }}</div>
            <div class="value">{{ m.value }}</div>
          </li>
        </ul>
      </div>
    </div>
  `,
});

const ChartPanel = defineComponent({
  name: 'ChartPanel',
  template: `<div class="board" id="chart"></div>`,
});

const AppRoot = defineComponent({
  name: 'AppRoot',
  components: { HeaderBar, SideBar, AnalysisPanel, InfoPanel, ChartPanel },
  data() {
    return {
      analysis: {
        title: 'AI Analysis',
        description: 'Apple inc. has a strong market position and solid financials. making it an attractive investment.',
        score: '7/10 - moderately attractive',
        pros: ['Strong market position'],
        cons: ['High P/S ratio'],
      },
      info: {
        ticker: 'AAPL',
        company: 'Apple Inc.',
        metrics: [
          { type: 'P/E', value: '28.7' },
          { type: 'P/S', value: '7.5' },
          { type: 'P/B', value: '40.1' },
          { type: 'EPS', value: '6.21' },
        ],
      },
    };
  },
  template: `
    <div>
      <HeaderBar />
      <div class="main_container">
        <SideBar />
        <main>
          <InfoPanel :ticker="info.ticker" :company="info.company" :metrics="info.metrics" />
          <ChartPanel />
          <AnalysisPanel 
            :title="analysis.title" 
            :description="analysis.description" 
            :score="analysis.score" 
            :pros="analysis.pros" 
            :cons="analysis.cons"
          />
        </main>
      </div>
    </div>
  `,
});

createApp(AppRoot).mount('#app');

