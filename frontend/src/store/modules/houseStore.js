import { dealCount } from "@/api/house";

const houseStore = {
  namespaced: true,
  state: {
    dealCount: [],
  },
  mutations: {
    SET_DEAL_COUNT: (state, dealCount) => {
      console.log("전"+state.dealCount);
      state.dealCount = dealCount;
      console.log("후"+state.dealCount);
    },
  },
  actions: {
    async getDealCount({ commit }, aptName) {
      await dealCount(
        aptName,
        ({ data }) => {
          commit("SET_DEAL_COUNT", []);
          console.log(data)
          commit("SET_DEAL_COUNT", data);
        },
        (error) => {
          console.log(error);
        }
      );
    },
  },
};

export default houseStore;
