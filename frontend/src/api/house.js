import http from '@/api/http';

async function dealCount(aptName, success, fail) {
  await http.get(`dealCountFind/${aptName}`).then(success).catch(fail);
}

export { dealCount };
